package com.example.turisapp.ui.reserva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turisapp.R;
import com.example.turisapp.modelo.Reserva;

import java.util.List;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ViewHolder> {

    private Context context;
    private List<Reserva> lista;
    private OnReservaClick listener;
    private String rol; // Cliente / Propietario / Admin

    public ReservaAdapter(Context context,
                          List<Reserva> lista,
                          OnReservaClick listener,
                          String rol) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
        this.rol = rol;
    }

    // ======================================================
    // CALLBACKS
    // ======================================================
    public interface OnReservaClick {
        void onVerDetalle(Reserva r);
        void onModificar(int position, Reserva r);
        void onPagar(int position, Reserva r);
        void onCancelar(int position, Reserva r);
        void onConfirmar(int position, Reserva r);
        void onRechazar(int position, Reserva r);
        void onConfirmarPago(int position, Reserva r);
    }

    public void setLista(List<Reserva> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    // ======================================================
    // VIEW HOLDER
    // ======================================================
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvLocalidad, tvPersona, tvFechas, tvMonto, tvEstado;
        Button btnAccion, btnSecundario;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitulo     = itemView.findViewById(R.id.tvTitulo);
            tvLocalidad  = itemView.findViewById(R.id.tvLocalidad);
            tvPersona    = itemView.findViewById(R.id.tvPersona);
            tvFechas     = itemView.findViewById(R.id.tvFechas);
            tvMonto      = itemView.findViewById(R.id.tvMonto);
            tvEstado     = itemView.findViewById(R.id.tvEstado);

            btnAccion    = itemView.findViewById(R.id.btnPagar);
            btnSecundario= itemView.findViewById(R.id.btnCancelar);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_reserva, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        Reserva r = lista.get(position);

        // --------------------------------------------------
        // NORMALIZAR ESTADO
        // --------------------------------------------------
        String estado = r.getEstado() != null
                ? r.getEstado().trim().replace(" ", "")
                : "";

        // --------------------------------------------------
        // DATOS BASE
        // --------------------------------------------------
        h.tvTitulo.setText(r.getAlojamientoTitulo());
        h.tvLocalidad.setText(r.getLocalidadNombre());
        h.tvFechas.setText(r.getFechaInicio() + " → " + r.getFechaFin());
        h.tvMonto.setText("$" + r.getMontoTotal());
        h.tvEstado.setText(r.getEstado());

        // --------------------------------------------------
        // PERSONA SEGÚN ROL ⭐
        // --------------------------------------------------
        if ("Admin".equalsIgnoreCase(rol)) {
            h.tvPersona.setText(
                    "Cliente: " + r.getNombreCliente()
                            + " | Propietario: " + r.getNombrePropietario()
            );
            h.tvPersona.setVisibility(View.VISIBLE);

        } else if ("Cliente".equalsIgnoreCase(rol)) {
            h.tvPersona.setText("Propietario: " + r.getNombrePropietario());
            h.tvPersona.setVisibility(View.VISIBLE);

        } else if ("Propietario".equalsIgnoreCase(rol)) {
            h.tvPersona.setText("Cliente: " + r.getNombreCliente());
            h.tvPersona.setVisibility(View.VISIBLE);

        } else {
            h.tvPersona.setVisibility(View.GONE);
        }


        // --------------------------------------------------
        // RESET BOTONES
        // --------------------------------------------------
        h.btnAccion.setVisibility(View.GONE);
        h.btnSecundario.setVisibility(View.GONE);
        h.btnAccion.setEnabled(true);
        h.btnAccion.setAlpha(1f);

        // ==================================================
        // CLIENTE
        // ==================================================
        if ("Cliente".equalsIgnoreCase(rol)) {

            switch (estado) {

                case "PendienteConfirmacion":
                    h.btnAccion.setVisibility(View.VISIBLE);
                    h.btnAccion.setText("Modificar");

                    h.btnSecundario.setVisibility(View.VISIBLE);
                    h.btnSecundario.setText("Cancelar");
                    break;

                case "PendientePago":
                    h.btnAccion.setVisibility(View.VISIBLE);
                    h.btnAccion.setText("Pagar");

                    h.btnSecundario.setVisibility(View.VISIBLE);
                    h.btnSecundario.setText("Cancelar");
                    break;

                case "Pagada":
                    h.btnAccion.setVisibility(View.VISIBLE);
                    h.btnAccion.setText("Pagado");
                    h.btnAccion.setEnabled(false);
                    h.btnAccion.setAlpha(0.5f);
                    break;
            }
        }

        // ==================================================
        // PROPIETARIO
        // ==================================================
        else if ("Propietario".equalsIgnoreCase(rol)) {

            switch (estado) {

                case "PendienteConfirmacion":
                    h.btnAccion.setVisibility(View.VISIBLE);
                    h.btnAccion.setText("Confirmar");

                    h.btnSecundario.setVisibility(View.VISIBLE);
                    h.btnSecundario.setText("Rechazar");
                    break;

                case "PendientePago":
                    h.btnAccion.setVisibility(View.VISIBLE);
                    h.btnAccion.setText("Confirmar pago");
                    break;
            }
        }

        // --------------------------------------------------
        // CLICK BOTÓN PRINCIPAL
        // --------------------------------------------------
        h.btnAccion.setOnClickListener(v -> {

            String txt = h.btnAccion.getText().toString();

            if ("Cliente".equalsIgnoreCase(rol)) {

                if ("Modificar".equals(txt)) {
                    listener.onModificar(position, r);

                } else if ("Pagar".equals(txt)) {
                    listener.onPagar(position, r);
                }

            } else if ("Propietario".equalsIgnoreCase(rol)) {

                if ("Confirmar".equals(txt)) {
                    listener.onConfirmar(position, r);

                } else if ("Confirmar pago".equals(txt)) {
                    listener.onConfirmarPago(position, r);
                }
            }
        });

        // --------------------------------------------------
        // CLICK BOTÓN SECUNDARIO
        // --------------------------------------------------
        h.btnSecundario.setOnClickListener(v -> {

            String txt = h.btnSecundario.getText().toString();

            if ("Cancelar".equals(txt)) {
                listener.onCancelar(position, r);

            } else if ("Rechazar".equals(txt)) {
                listener.onRechazar(position, r);
            }
        });

        // Click en la tarjeta
        h.itemView.setOnClickListener(v -> listener.onVerDetalle(r));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }
}
