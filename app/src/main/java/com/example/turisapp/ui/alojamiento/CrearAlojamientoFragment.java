package com.example.turisapp.ui.alojamiento;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.turisapp.databinding.FragmentCrearAlojamientoBinding;
import com.example.turisapp.modelo.Alojamiento;
import com.example.turisapp.modelo.Localidad;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.ui.localidad.ListarLocalidadViewModel;

import java.util.ArrayList;
import java.util.List;

public class CrearAlojamientoFragment extends Fragment {

    private FragmentCrearAlojamientoBinding binding;
    private CrearAlojamientoViewModel vm;

    // -----------------------------
    // IMÁGENES
    // -----------------------------
    private List<Uri> imagenesSeleccionadas = new ArrayList<>();

    // -----------------------------
    // SPINNER LOCALIDAD
    // -----------------------------
    private List<Localidad> localidades = new ArrayList<>();
    private int localidadSeleccionadaId = -1;

    // -----------------------------
    // SPINNER TIPO
    // -----------------------------
    private String tipoSeleccionado = "";

    private final ActivityResultLauncher<String[]> pickImages =
            registerForActivityResult(
                    new ActivityResultContracts.OpenMultipleDocuments(),
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

        // -----------------------------
        // VALIDAR ROL
        // -----------------------------
        String rol = ApiClient.leerRol(requireContext()); // Cliente / Propietario / Administrador

        if ("Cliente".equalsIgnoreCase(rol)) {
            binding.btnCrearAlojamiento.setEnabled(false);
            binding.btnSeleccionarImagenes.setEnabled(false);
            binding.tvMensaje.setText(
                    "Solo Propietarios o Administradores pueden crear alojamientos"
            );
            return;
        }

        // -----------------------------
        // SPINNERS
        // -----------------------------
        configurarSpinnerTipo();
        configurarSpinnerLocalidad();

        // -----------------------------
        // IMÁGENES
        // -----------------------------
        binding.btnSeleccionarImagenes.setOnClickListener(v ->
                pickImages.launch(new String[]{"image/*"})
        );

        // -----------------------------
        // CREAR ALOJAMIENTO
        // -----------------------------
        binding.btnCrearAlojamiento.setOnClickListener(v -> {

            int propietarioId =
                    ApiClient.leerUsuarioId(requireContext());

            Alojamiento a = new Alojamiento(
                    binding.etTitulo.getText().toString(),
                    tipoSeleccionado,
                    binding.etDescripcion.getText().toString(),
                    binding.etDireccion.getText().toString(),
                    parseDouble(binding.etPrecio.getText().toString()),
                    1,
                    parseInt(binding.etHuespedes.getText().toString()),
                    parseInt(binding.etHabitaciones.getText().toString()),
                    binding.swPileta.isChecked() ? 1 : 0,
                    binding.swCochera.isChecked() ? 1 : 0,
                    "Activo",
                    localidadSeleccionadaId,
                    propietarioId,
                    ""
            );

            vm.crearAlojamiento(a, imagenesSeleccionadas);
        });

        // -----------------------------
        // MENSAJES
        // -----------------------------
        vm.getMensaje().observe(getViewLifecycleOwner(),
                m -> binding.tvMensaje.setText(m));
    }

    // =====================================================
    // SPINNER TIPO
    // =====================================================
    private void configurarSpinnerTipo() {

        String[] tipos = {"Casa", "Departamento", "Cabaña", "Hotel"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        tipos
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        binding.spTipo.setAdapter(adapter);

        binding.spTipo.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        tipoSeleccionado = tipos[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        tipoSeleccionado = "";
                    }
                });
    }

    // =====================================================
    // SPINNER LOCALIDAD
    // =====================================================
    private void configurarSpinnerLocalidad() {

        ListarLocalidadViewModel lvm =
                new ViewModelProvider(this)
                        .get(ListarLocalidadViewModel.class);

        lvm.getLocalidades().observe(getViewLifecycleOwner(), lista -> {

            localidades = lista;

            List<String> nombres = new ArrayList<>();
            for (Localidad l : lista) {
                nombres.add(l.getNombre());
            }

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            nombres
                    );

            adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
            );

            binding.spLocalidad.setAdapter(adapter);
        });

        binding.spLocalidad.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        localidadSeleccionadaId =
                                localidades.get(position).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        localidadSeleccionadaId = -1;
                    }
                });

        lvm.cargarLocalidades();
    }

    // =====================================================
    // HELPERS
    // =====================================================
    private int parseInt(String n) {
        try { return Integer.parseInt(n); }
        catch (Exception e) { return -1; }
    }

    private double parseDouble(String n) {
        try { return Double.parseDouble(n); }
        catch (Exception e) { return -1; }
    }
}
