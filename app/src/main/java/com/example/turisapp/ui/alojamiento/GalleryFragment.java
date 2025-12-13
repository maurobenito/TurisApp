package com.example.turisapp.ui.alojamiento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.turisapp.R;
import com.example.turisapp.databinding.FragmentGalleryBinding;
import com.example.turisapp.modelo.Alojamiento;

import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private GalleryViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.btnAgregarAlojamiento.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.alojamientosFragment);
        });

        vm.getListaAlojamientos().observe(getViewLifecycleOwner(), new Observer<List<Alojamiento>>() {
            @Override
            public void onChanged(List<Alojamiento> alojamientos) {
                AlojamientoAdapter adapter =
                        new AlojamientoAdapter(alojamientos, getContext(), getLayoutInflater());

                GridLayoutManager glm = new GridLayoutManager(
                        getContext(),
                        2,
                        GridLayoutManager.VERTICAL,
                        false
                );

                binding.listaAlojamientos.setLayoutManager(glm);
                binding.listaAlojamientos.setAdapter(adapter);
            }
        });

        vm.obtenerAlojamientos();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
