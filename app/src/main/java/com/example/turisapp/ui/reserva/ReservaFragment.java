package com.example.turisapp.ui.reserva;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.turisapp.R;
import com.example.turisapp.databinding.FragmentReservaBinding;
import com.example.turisapp.modelo.Alojamiento;

import java.util.Calendar;

public class ReservaFragment extends Fragment {

    private FragmentReservaBinding binding;
    private ReservaViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservaBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(ReservaViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // ---------------------------------------------------------
        // RECIBIR ALOJAMIENTO
        // ---------------------------------------------------------
        Alojamiento alojamiento = (Alojamiento) getArguments().getSerializable("alojamiento");
        vm.iniciar(alojamiento);

        vm.tituloAlojamiento.observe(getViewLifecycleOwner(),
                binding.tvTituloAlojamiento::setText);

        vm.total.observe(getViewLifecycleOwner(),
                total -> binding.tvTotal.setText("Total: $" + total));

        vm.fechaInicioStr.observe(getViewLifecycleOwner(),
                f -> binding.btnFechaInicio.setText("Inicio: " + f));

        vm.fechaFinStr.observe(getViewLifecycleOwner(),
                f -> binding.btnFechaFin.setText("Fin: " + f));

        vm.mensaje.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        vm.reservaExitosa.observe(getViewLifecycleOwner(), ok -> {
            androidx.navigation.NavOptions opts = new androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.mobile_navigation, false)
                    .build();

            Navigation.findNavController(view)
                    .navigate(R.id.action_reservaFragment_to_alojamientoFragment, null, opts);
        });

        binding.btnFechaInicio.setOnClickListener(v -> mostrarDatePickerInicio());
        binding.btnFechaFin.setOnClickListener(v -> mostrarDatePickerFin());
        binding.btnConfirmar.setOnClickListener(v -> vm.confirmarReserva());
    }


    private void mostrarDatePickerInicio() {
        Calendar c = Calendar.getInstance();

        DatePickerDialog dp = new DatePickerDialog(
                requireContext(),
                this::onFechaInicioSeleccionada,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        dp.show();
    }

    private void onFechaInicioSeleccionada(DatePicker v, int y, int m, int d) {
        Calendar sel = Calendar.getInstance();
        sel.set(y, m, d);
        vm.seleccionarFechaInicio(sel.getTime());
    }


    private void mostrarDatePickerFin() {
        Calendar c = Calendar.getInstance();

        DatePickerDialog dp = new DatePickerDialog(
                requireContext(),
                this::onFechaFinSeleccionada,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        dp.show();
    }

    private void onFechaFinSeleccionada(DatePicker v, int y, int m, int d) {
        Calendar sel = Calendar.getInstance();
        sel.set(y, m, d);
        vm.seleccionarFechaFin(sel.getTime());
    }
}
