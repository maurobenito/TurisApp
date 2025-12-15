package com.example.turisapp.ui.pago;

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

import com.example.turisapp.databinding.FragmentPagoBinding;
import com.example.turisapp.modelo.Pago;
import com.example.turisapp.request.ApiClient;

import java.util.ArrayList;

public class PagoFragment extends Fragment {

    private FragmentPagoBinding binding;
    private PagoViewModel vm;
    private PagoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPagoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(PagoViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        String rol = ApiClient.leerRol(requireContext()); // Cliente / Propietario

        adapter = new PagoAdapter(
                new ArrayList<>(),
                rol,
                new PagoAdapter.OnPagoAccion() {
                    @Override
                    public void confirmar(Pago p) {
                        vm.confirmarPago(p.getReservaId());
                    }

                    @Override
                    public void rechazar(Pago p) {
                        vm.rechazarPago(p.getId(), p.getReservaId());
                    }
                }
        );

        binding.rvPagos.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPagos.setAdapter(adapter);

        // -------------------------
        // OBSERVERS
        // -------------------------
        vm.pagos.observe(getViewLifecycleOwner(), lista ->
                adapter.setLista(lista)
        );

        vm.mensaje.observe(getViewLifecycleOwner(), m ->
                Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show()
        );

        // -------------------------
        vm.cargarPagos();
    }
}
