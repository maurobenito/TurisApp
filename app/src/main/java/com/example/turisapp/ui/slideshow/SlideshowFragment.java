package com.example.turisapp.ui.slideshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.turisapp.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel vm;
    private FragmentSlideshowBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(SlideshowViewModel.class);

        // ❌ Cancelar → no hacer nada / volver
        binding.btnCancelar.setOnClickListener(v ->
                requireActivity().onBackPressed()
        );

        // ✅ SALIR → borrar sesión y cerrar app
        binding.btnSalir.setOnClickListener(v -> cerrarSesionYSalir());

        return binding.getRoot();
    }

    // =====================================================
    // LOGOUT TOTAL + CERRAR APP
    // =====================================================
    private void cerrarSesionYSalir() {

        // 🧹 1. LIMPIAR SHARED PREFERENCES
        SharedPreferences sp =
                requireContext().getSharedPreferences("datos", Context.MODE_PRIVATE);

        sp.edit().clear().apply();

        // ❌ 2. CERRAR LA APP COMPLETA
        requireActivity().finishAffinity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
