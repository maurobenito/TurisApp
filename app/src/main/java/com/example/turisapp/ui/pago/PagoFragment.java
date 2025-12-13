package com.example.turisapp.ui.pago;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.turisapp.databinding.FragmentPagoBinding;
import com.example.turisapp.modelo.Pago;

import java.util.List;

public class PagoFragment extends Fragment {

    private FragmentPagoBinding binding;
    private PagoViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(PagoViewModel.class);
        binding = FragmentPagoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vm.getListaPagos().observe(getViewLifecycleOwner(), pagos -> {

            PagoAdapter adapter = new PagoAdapter(
                    pagos,
                    getContext(),
                    getLayoutInflater()
            );

            binding.rvPagos.setLayoutManager(
                    new LinearLayoutManager(getContext())
            );

            binding.rvPagos.setAdapter(adapter);
        });

        vm.obtenerPagos();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
