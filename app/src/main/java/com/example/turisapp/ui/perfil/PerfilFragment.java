package com.example.turisapp.ui.perfil;

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

import com.bumptech.glide.Glide;
import com.example.turisapp.R;
import com.example.turisapp.databinding.FragmentPerfilBinding;
import com.example.turisapp.modelo.Usuario;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel vm;
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(PerfilViewModel.class);

        configurarSpinners();
        configurarAvatarPicker();
        observarViewModel();
        configurarBoton();

        vm.obtenerPerfil(); // carga datos al entrar

        return binding.getRoot();
    }

    // ------------------------------------------------------------
    // SPINNERS
    // ------------------------------------------------------------
    private void configurarSpinners() {

        String[] roles = {"Administrador", "Propietario", "Cliente"};
        android.widget.ArrayAdapter<String> adapterRol =
                new android.widget.ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, roles);
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spRol.setAdapter(adapterRol);

        String[] estados = {"Activo", "Inactivo"};
        android.widget.ArrayAdapter<String> adapterEstado =
                new android.widget.ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, estados);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spEstado.setAdapter(adapterEstado);
    }

    // ------------------------------------------------------------
    // AVATAR
    // ------------------------------------------------------------
    private void configurarAvatarPicker() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> vm.actualizarAvatar(uri)
        );

        binding.imgAvatar.setOnClickListener(v ->
                pickImageLauncher.launch("image/*")
        );
    }

    // ------------------------------------------------------------
    // OBSERVADORES
    // ------------------------------------------------------------
    private void observarViewModel() {

        vm.getUsuario().observe(getViewLifecycleOwner(), this::mostrarUsuario);

        vm.getEstado().observe(getViewLifecycleOwner(), this::bloquearCampos);

        vm.getNombreBoton().observe(getViewLifecycleOwner(),
                texto -> binding.btPerfil.setText(texto));
    }

    private void mostrarUsuario(Usuario u) {
        binding.etNombre.setText(u.getNombre());
        binding.etApellido.setText(u.getApellido());
        binding.etDni.setText(u.getDni());
        binding.etTelefono.setText(u.getTelefono());
        binding.etEmail.setText(u.getEmail());
        binding.etClave.setText(""); // NUNCA mostrar la clave real


        binding.spRol.setSelection(vm.getIndexRol(u.getRol()));
        binding.spEstado.setSelection(vm.getIndexEstado(u.getEstado()));

        String url = "http://192.168.1.7/turisapp/api/uploads/avatars/" + u.getAvatar();

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(binding.imgAvatar);
    }

    private void bloquearCampos(Boolean enabled) {
        binding.etNombre.setEnabled(enabled);
        binding.etApellido.setEnabled(enabled);
        binding.etDni.setEnabled(enabled);
        binding.etTelefono.setEnabled(enabled);
        binding.etEmail.setEnabled(enabled);
        binding.etClave.setEnabled(enabled);
        binding.spRol.setEnabled(enabled);
        binding.spEstado.setEnabled(enabled);
    }


    private void configurarBoton() {
        binding.btPerfil.setOnClickListener(v -> {

            String modo = vm.getNombreBoton().getValue();
            if (modo == null) modo = "EDITAR"; // fallback seguro

            vm.cambioBoton(
                    modo,
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
