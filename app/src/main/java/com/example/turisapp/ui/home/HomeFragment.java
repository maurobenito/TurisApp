package com.example.turisapp.ui.home;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.turisapp.R;
import com.example.turisapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.btnAlojamientos.setOnClickListener(v -> {

            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_home, true) // 👈 ESTE ES EL ID CORRECTO
                    .build();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.alojamientosFragment, null, navOptions);
        });


        // Login (normal, vuelve a Home si apretás atrás)
        binding.btnLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.loginFragment)
        );

        // Registro (normal)
        binding.btnRegistro.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.registroFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
