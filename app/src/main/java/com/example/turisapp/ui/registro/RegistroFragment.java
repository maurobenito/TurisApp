package com.example.turisapp.ui.registro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.turisapp.databinding.FragmentRegistroBinding;

public class RegistroFragment extends Fragment {

    private FragmentRegistroBinding binding;
    private RegistroViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(RegistroViewModel.class);

        configurarSpinners();
        configurarBoton();

        vm.getMensaje().observe(getViewLifecycleOwner(), mensaje ->
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show()
        );

        return binding.getRoot();
    }

    private void configurarSpinners() {
        String[] roles = {"Cliente", "Propietario"};
        ArrayAdapter<String> adapterRol = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                roles
        );
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spRol.setAdapter(adapterRol);

        String[] estados = {"Activo", "Inactivo"};
        ArrayAdapter<String> adapterEstado = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                estados
        );
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spEstado.setAdapter(adapterEstado);
    }

    private void configurarBoton() {
        binding.btRegistrar.setOnClickListener(v -> {

            vm.crearUsuario(
                    binding.etNombre.getText().toString(),
                    binding.etApellido.getText().toString(),
                    binding.etDni.getText().toString(),
                    binding.etTelefono.getText().toString(),
                    binding.etEmail.getText().toString(),
                    binding.etClave.getText().toString(),
                    binding.spRol.getSelectedItem().toString(),
                    binding.spEstado.getSelectedItem().toString()
            );
        });
    }
}
