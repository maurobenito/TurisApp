package com.example.turisapp.ui.localidad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.turisapp.databinding.FragmentListarLocalidadBinding;

public class ListarLocalidadFragment extends Fragment {

    private FragmentListarLocalidadBinding binding;
    private ListarLocalidadViewModel vm;
    private LocalidadAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentListarLocalidadBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(ListarLocalidadViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        adapter = new LocalidadAdapter();

        adapter.setOnClickListener(vm::seleccionar);


        adapter.setOnLongClickListener(localidad -> {

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar localidad")
                    .setMessage("¿Seguro que querés eliminar \"" + localidad.getNombre() + "\"?")
                    .setPositiveButton("Eliminar", (dialog, which) -> vm.eliminar(localidad))
                    .setNegativeButton("Cancelar", null)
                    .show();
        });


        binding.rvLocalidades.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvLocalidades.setAdapter(adapter);


        vm.getLocalidades().observe(getViewLifecycleOwner(), adapter::setLista);


        vm.getLocalidadSeleccionada().observe(getViewLifecycleOwner(), l -> {
            binding.etNombre.setVisibility(View.VISIBLE);
            binding.btnGuardar.setVisibility(View.VISIBLE);
            binding.etNombre.setText(l.getNombre());
        });


        vm.getMensaje().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());


        binding.btnAgregar.setOnClickListener(v -> vm.nuevaLocalidad());


        binding.btnGuardar.setOnClickListener(v ->
                vm.guardar(binding.etNombre.getText().toString())
        );

        vm.cargarLocalidades();
    }
}
