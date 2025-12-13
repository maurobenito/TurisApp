package com.example.turisapp.ui.alojamiento;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.turisapp.R;
import com.example.turisapp.databinding.FragmentDetalleAlojamientoBinding;
import com.example.turisapp.request.ApiClient;

public class DetalleAlojamientoFragment extends Fragment {

    private FragmentDetalleAlojamientoBinding binding;
    private DetalleAlojamientoViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDetalleAlojamientoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(DetalleAlojamientoViewModel.class);

        observar();
        vm.cargarAlojamiento(getArguments());   // 👉 el VM decide todo

        return binding.getRoot();
    }

    private void observar() {

        // =========================
        // DATOS DEL ALOJAMIENTO
        // =========================
        vm.alojamiento.observe(getViewLifecycleOwner(), a -> {

            binding.tvTitulo.setText(a.getTitulo());
            binding.tvTipo.setText(a.getTipo());
            binding.tvPrecio.setText("$ " + a.getPrecioPorDia());
            binding.tvDescripcion.setText(a.getDescripcion());
            binding.tvDireccion.setText(a.getDireccion());

            binding.tvHuespedes.setText(a.getCantidadHuespedes() + " huéspedes");
            binding.tvHabitaciones.setText(a.getHabitaciones() + " hab.");

            binding.tvPileta.setText(a.getPileta() == 1 ? "Pileta" : "Sin pileta");
            binding.tvCochera.setText(a.getCochera() == 1 ? "Cochera" : "Sin cochera");
        });

        // =========================
        // IMAGEN PRINCIPAL (SIEMPRE UNA)
        // =========================
        vm.imagenPrincipal().observe(getViewLifecycleOwner(), ruta -> {

            Log.d("IMG_DETALLE", "Ruta recibida = " + ruta);
            Log.d("IMG_DETALLE", "URL final = " +
                    (ruta == null ? "PLACEHOLDER" : ApiClient.IMAGE_URL + ruta));

            Glide.with(requireContext())
                    .load(ruta == null
                            ? R.drawable.placeholder
                            : ApiClient.IMAGE_URL + ruta)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .into(binding.imgPrincipal);
        });


        vm.imagenes().observe(getViewLifecycleOwner(), adapter -> {

            binding.rvMiniaturas.setLayoutManager(
                    new LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                    )
            );

            binding.rvMiniaturas.setAdapter(adapter);
        });

        vm.localidadTexto.observe(getViewLifecycleOwner(),
                txt -> binding.tvLocalidad.setText("📍 " + txt));

        vm.botonTexto.observe(getViewLifecycleOwner(),
                t -> binding.btnAccion.setText(t));

        vm.botonColor.observe(getViewLifecycleOwner(),
                c -> binding.btnAccion.setBackgroundTintList(
                        ContextCompat.getColorStateList(requireContext(), c)
                ));

        vm.tituloPantalla.observe(getViewLifecycleOwner(),
                t -> requireActivity().setTitle(t));

        vm.mensaje.observe(getViewLifecycleOwner(),
                m -> android.widget.Toast
                        .makeText(requireContext(), m, android.widget.Toast.LENGTH_SHORT)
                        .show());

        binding.btnAccion.setOnClickListener(v -> vm.onAccionClick());

        vm.navActionId.observe(getViewLifecycleOwner(), actionId -> {
            Bundle b = new Bundle();
            b.putSerializable("alojamiento", vm.alojamiento.getValue());
            Navigation.findNavController(binding.getRoot())
                    .navigate(actionId, b);
        });
    }
}
