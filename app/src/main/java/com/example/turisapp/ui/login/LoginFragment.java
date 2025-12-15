package com.example.turisapp.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.turisapp.R;
import com.example.turisapp.request.ApiClient;

public class LoginFragment extends Fragment {

    private EditText etEmail, etClave;
    private Button btLogin;
    private LoginViewModel vm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etEmail = view.findViewById(R.id.etEmail);
        etClave = view.findViewById(R.id.etPassword);
        btLogin = view.findViewById(R.id.btnLogin);

        vm = new ViewModelProvider(this).get(LoginViewModel.class);

        // 🔹 Guardar token cuando llegue
        vm.getTokenOK().observe(getViewLifecycleOwner(), token -> {
            ApiClient.guardarToken(requireContext(), token);
        });

        TextView txtRegistro = view.findViewById(R.id.txtRegistro);

        txtRegistro.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(R.id.registroFragment);
        });
        vm.getLoginOK().observe(getViewLifecycleOwner(), usuario -> {

            // Guarda datos del usuario
            SharedPreferences sp = requireContext().getSharedPreferences("datos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putInt("id", usuario.getId());
            editor.putString("nombre", usuario.getNombre());
            editor.putString("apellido", usuario.getApellido());
            editor.putString("email", usuario.getEmail());
            editor.putString("clave", usuario.getClave());
            editor.putString("rol", usuario.getRol());
            editor.putString("avatar", usuario.getAvatar());
            editor.putString("estado", usuario.getEstado());
            editor.putString("dni", usuario.getDni());
            editor.putString("telefono", usuario.getTelefono());
            editor.apply();

            Toast.makeText(getContext(),
                    "Bienvenido " + usuario.getNombre() + " (" + usuario.getRol() + ")",
                    Toast.LENGTH_LONG).show();

            // Navegar al home SOLO UNA VEZ
            Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_to_alojamientosFragment);
        });

        // Manejo de error
        vm.getLoginError().observe(getViewLifecycleOwner(), mensaje -> {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
        });

        btLogin.setOnClickListener(v -> {
            vm.login(
                    etEmail.getText().toString(),
                    etClave.getText().toString()
            );
        });

        return view;

    }
}
