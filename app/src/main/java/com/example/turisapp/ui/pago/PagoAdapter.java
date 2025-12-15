package com.example.turisapp.ui.pago;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turisapp.R;
import com.example.turisapp.modelo.Pago;

import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.ViewHolderPago> {

    private List<Pago> lista;
    private String rol; // Cliente / Propietario / Admin
    private OnPagoAccion listener;

    public PagoAdapter(List<Pago> lista, String rol, OnPagoAccion listener) {
        this.lista = lista;
        this.rol = rol;
        this.listener = listener;
    }

    public interface OnPagoAccion {
        void confirmar(Pago p);
        void rechazar(Pago p);
    }

    public void setLista(List<Pago> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderPago onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pago, parent, false);
        return new ViewHolderPago(view);
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPago h, int position) {

        Pago p = lista.get(position);

        // ===============================
        // ALOJAMIENTO
        // ===============================
        h.tvAlojamiento.setText(p.getAlojamientoTitulo());

        // ===============================
        // PERSONA (MISMA LÓGICA QUE RESERVA ✅)
        // ===============================
        h.tvPersona.setVisibility(View.VISIBLE);

        if ("Admin".equalsIgnoreCase(rol) || "Administrador".equalsIgnoreCase(rol)) {

            h.tvPersona.setText(
                    "Cliente: " + p.getClienteNombre() + " " + p.getClienteApellido()
                            + " | Propietario: " + p.getPropietarioNombre() + " " + p.getPropietarioApellido()
            );

        } else if ("Cliente".equalsIgnoreCase(rol)) {

            h.tvPersona.setText(
                    "Propietario: " + p.getPropietarioNombre() + " " + p.getPropietarioApellido()
            );

        } else if ("Propietario".equalsIgnoreCase(rol)) {

            h.tvPersona.setText(
                    "Cliente: " + p.getClienteNombre() + " " + p.getClienteApellido()
            );
        }

        // ===============================
        // FECHAS DE LA RESERVA
        // ===============================
        h.tvFechas.setText(
                p.getFechaInicio() + " → " + p.getFechaFin()
        );

        // ===============================
        // MONTO / FECHA PAGO / ESTADO
        // ===============================
        h.tvMonto.setText("$" + p.getMonto());
        h.tvFechaPago.setText(p.getFechaPago());
        h.tvEstado.setText(p.getEstadoPago());

        // ===============================
        // BOTONES (solo propietario)
        // ===============================
        h.btnConfirmar.setVisibility(View.GONE);
        h.btnRechazar.setVisibility(View.GONE);

        if ("Propietario".equalsIgnoreCase(rol)
                && "Pendiente".equalsIgnoreCase(p.getEstadoPago())
                && listener != null) {

            h.btnConfirmar.setVisibility(View.VISIBLE);
            h.btnRechazar.setVisibility(View.VISIBLE);

            h.btnConfirmar.setOnClickListener(v -> listener.confirmar(p));
            h.btnRechazar.setOnClickListener(v -> listener.rechazar(p));
        }
    }

    // =================================================
    // VIEW HOLDER
    // =================================================
    static class ViewHolderPago extends RecyclerView.ViewHolder {

        TextView tvAlojamiento, tvPersona, tvFechas,
                tvMonto, tvFechaPago, tvEstado;
        Button btnConfirmar, btnRechazar;

        public ViewHolderPago(@NonNull View itemView) {
            super(itemView);
            tvAlojamiento = itemView.findViewById(R.id.tvAlojamiento);
            tvPersona     = itemView.findViewById(R.id.tvPersona);
            tvFechas      = itemView.findViewById(R.id.tvFecha);
            tvMonto       = itemView.findViewById(R.id.tvMonto);
            tvFechaPago   = itemView.findViewById(R.id.tvFecha);
            tvEstado      = itemView.findViewById(R.id.tvEstado);
            btnConfirmar  = itemView.findViewById(R.id.btnConfirmar);
            btnRechazar   = itemView.findViewById(R.id.btnRechazar);
        }
    }
}
