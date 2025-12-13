package com.example.turisapp.ui.alojamiento;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.turisapp.databinding.FragmentCrearAlojamientoBinding;
import com.example.turisapp.modelo.Alojamiento;

import java.util.ArrayList;
import java.util.List;

public class CrearAlojamientoFragment extends Fragment {

    private FragmentCrearAlojamientoBinding binding;
    private CrearAlojamientoViewModel vm;

    private List<Uri> imagenesSeleccionadas = new ArrayList<>();

    private final ActivityResultLauncher<String[]> pickImages =
            registerForActivityResult(new ActivityResultContracts.OpenMultipleDocuments(),
                    uris -> imagenesSeleccionadas = uris
            );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCrearAlojamientoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(CrearAlojamientoViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.btnSeleccionarImagenes.setOnClickListener(v ->
                pickImages.launch(new String[]{"image/*"})
        );

        binding.btnCrearAlojamiento.setOnClickListener(v -> {

            Alojamiento a = new Alojamiento(
                    binding.etTitulo.getText().toString(),
                    binding.etTipo.getText().toString(),
                    binding.etDescripcion.getText().toString(),
                    binding.etDireccion.getText().toString(),
                    parseDouble(binding.etPrecio.getText().toString()),
                    1,
                    parseInt(binding.etHuespedes.getText().toString()),
                    parseInt(binding.etHabitaciones.getText().toString()),
                    binding.swPileta.isChecked() ? 1 : 0,
                    binding.swCochera.isChecked() ? 1 : 0,
                    "Activo",
                    parseInt(binding.etLocalidad.getText().toString()),
                    parseInt(binding.etPropietario.getText().toString()),
                    ""
            );

            vm.crearAlojamiento(a, imagenesSeleccionadas);
        });

        vm.getMensaje().observe(getViewLifecycleOwner(),
                m -> binding.tvMensaje.setText(m));
    }

    private int parseInt(String n) {
        try { return Integer.parseInt(n); }
        catch (Exception e) { return -1; }
    }

    private double parseDouble(String n) {
        try { return Double.parseDouble(n); }
        catch (Exception e) { return -1; }
    }
}
