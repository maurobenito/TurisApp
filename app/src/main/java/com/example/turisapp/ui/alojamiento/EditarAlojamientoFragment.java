
package com.example.turisapp.ui.alojamiento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.turisapp.R;
import com.example.turisapp.databinding.FragmentEditarAlojamientoBinding;
import com.example.turisapp.request.ApiClient;

public class EditarAlojamientoFragment extends Fragment {

    private FragmentEditarAlojamientoBinding binding;
    private EditarAlojamientoViewModel vm;

    private final ActivityResultLauncher<String[]> pickImages =
            registerForActivityResult(
                    new ActivityResultContracts.OpenMultipleDocuments(),
                    uris -> vm.setImagenesSeleccionadas(uris)
            );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentEditarAlojamientoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(EditarAlojamientoViewModel.class);

        observar();
        vm.cargarAlojamiento(getArguments());

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(),
                        new androidx.activity.OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {

                                androidx.navigation.fragment.NavHostFragment
                                        .findNavController(EditarAlojamientoFragment.this)
                                        .navigate(R.id.listaAlojamientos);
                            }
                        });
    }

    private void observar() {

        // =========================
        // DATOS
        // =========================
        vm.getAlojamiento().observe(getViewLifecycleOwner(), a -> {

            binding.etTitulo.setText(a.getTitulo());
            binding.etDescripcion.setText(a.getDescripcion());
            binding.etDireccion.setText(a.getDireccion());
            binding.etPrecio.setText(String.valueOf(a.getPrecioPorDia()));
            binding.etHuespedes.setText(String.valueOf(a.getCantidadHuespedes()));
            binding.etHabitaciones.setText(String.valueOf(a.getHabitaciones()));

            binding.swPileta.setChecked(a.getPileta() == 1);
            binding.swCochera.setChecked(a.getCochera() == 1);
            binding.swDisponible.setChecked(a.getDisponible() == 1);
        });

        // =========================
        // IMAGEN PRINCIPAL
        // =========================
        vm.getImagenPrincipal().observe(getViewLifecycleOwner(), ruta -> {

            Glide.with(requireContext())
                    .load(ruta == null
                            ? R.drawable.placeholder
                            : ApiClient.IMAGE_URL + ruta)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .into(binding.imgPrincipal);
        });

        // =========================
        // GALERÍA (IGUAL QUE DETALLE)
        // =========================
        vm.getImagenes().observe(getViewLifecycleOwner(), adapter -> {

            binding.rvImagenes.setLayoutManager(
                    new LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                    )
            );

            binding.rvImagenes.setAdapter(adapter);
        });

        // =========================
        // ACCIONES
        // =========================
        binding.btnAgregarImagenes.setOnClickListener(v ->
                pickImages.launch(new String[]{"image/*"}));

        binding.btnGuardar.setOnClickListener(v -> {

            vm.actualizarCampos(
                    binding.etTitulo.getText().toString(),
                    binding.etDescripcion.getText().toString(),
                    binding.etDireccion.getText().toString(),
                    parseDouble(binding.etPrecio.getText().toString()),
                    parseInt(binding.etHuespedes.getText().toString()),
                    parseInt(binding.etHabitaciones.getText().toString()),
                    binding.swPileta.isChecked(),
                    binding.swCochera.isChecked(),
                    binding.swDisponible.isChecked()
            );

            vm.guardarTodo();
        });
        vm.getMensaje().observe(getViewLifecycleOwner(), m -> {
            if (m == null) return;

            Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show();

            if (m.equals("Alojamiento actualizado")
                    || m.equals("Alojamiento eliminado")) {

                androidx.navigation.fragment.NavHostFragment
                        .findNavController(this)
                        .navigateUp();
            }
        });


        binding.btnEliminarAlojamiento.setOnClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar alojamiento")
                    .setMessage("¿Seguro que desea eliminar este alojamiento?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        vm.eliminarAlojamiento();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

    }


    private int parseInt(String n) {
        try { return Integer.parseInt(n); }
        catch (Exception e) { return 0; }
    }

    private double parseDouble(String n) {
        try { return Double.parseDouble(n); }
        catch (Exception e) { return 0; }
    }
}
