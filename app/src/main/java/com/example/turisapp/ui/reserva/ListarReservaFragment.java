package com.example.turisapp.ui.reserva;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.turisapp.R;
import com.example.turisapp.databinding.FragmentListarReservaBinding;
import com.example.turisapp.modelo.Reserva;

import java.util.ArrayList;

public class ListarReservaFragment extends Fragment {

    private FragmentListarReservaBinding binding;
    private ListarReservaViewModel vm;
    private ReservaAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentListarReservaBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(ListarReservaViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        adapter = new ReservaAdapter(
                requireContext(),
                new ArrayList<>(),
                new ReservaAdapter.OnReservaClick() {

                    @Override
                    public void onCancelar(int position, Reserva r) {
                        vm.cancelarReserva(r);
                    }

                    @Override
                    public void onVerDetalle(Reserva r) {
                        // acción opcional
                    }

                    @Override
                    public void onPagar(int position, Reserva r) {
                        mostrarDialogoPago(r);
                    }
                }
        );

        binding.recyclerReservas.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerReservas.setAdapter(adapter);

        vm.reservas.observe(getViewLifecycleOwner(), lista ->
                adapter.setLista(lista)
        );

        vm.mensaje.observe(getViewLifecycleOwner(), m ->
                Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show()
        );

        vm.cancelacionExitosa.observe(getViewLifecycleOwner(), ok ->
                vm.cargarReservas()
        );

        vm.cargarReservas();
    }

    // --------------------------------------------------------------------
    // DIÁLOGO DE PAGO (TAMBIÉN SIN IF)
    // --------------------------------------------------------------------
    private void mostrarDialogoPago(Reserva r) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_pago, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        EditText etMedio = view.findViewById(R.id.etMedioDePago);
        EditText etMonto = view.findViewById(R.id.etMonto);
        Button btnConfirmar = view.findViewById(R.id.btnConfirmarPago);

        etMonto.setText(String.valueOf(r.getMontoTotal()));

        btnConfirmar.setOnClickListener(v -> {

            String medio = etMedio.getText().toString();
            double monto = Double.parseDouble(etMonto.getText().toString());

            dialog.dismiss();

            vm.registrarPago(r.getId(), monto, medio);
        });

        dialog.show();
    }
}
