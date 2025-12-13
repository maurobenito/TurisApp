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


    public ReservaAdapter(Context context, List<Reserva> lista, OnReservaClick listener) {
        this.context = context;
        this.lista = lista;
        this.listener = listener;
    }

    public interface OnReservaClick {
        void onCancelar(int position, Reserva r);
        void onVerDetalle(Reserva r);
        void onPagar(int position, Reserva r);
    }


    public void setLista(List<Reserva> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvFechas, tvMonto, tvEstado;
        Button btnCancelar, btnPagar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvFechas = itemView.findViewById(R.id.tvFechas);
            tvMonto = itemView.findViewById(R.id.tvMonto);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
            btnPagar = itemView.findViewById(R.id.btnPagar);
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reserva r = lista.get(position);

        // Título del Alojamiento
        holder.tvTitulo.setText(r.getAlojamientoTitulo());

        // Fechas
        holder.tvFechas.setText(r.getFechaInicio() + " → " + r.getFechaFin());

        // Monto
        holder.tvMonto.setText("Monto: $" + r.getMontoTotal());

        // Estado
        holder.tvEstado.setText("Estado: " + r.getEstado());

        // Si ya está pagado, deshabilitar botón
        if ("Pagado".equalsIgnoreCase(r.getEstado())) {
            holder.btnPagar.setEnabled(false);
            holder.btnPagar.setText("Pagado");
            holder.btnPagar.setAlpha(0.5f);
        } else {
            holder.btnPagar.setEnabled(true);
            holder.btnPagar.setText("Pagar");
            holder.btnPagar.setAlpha(1f);
        }

        // EVENTOS
        holder.btnCancelar.setOnClickListener(v ->
                listener.onCancelar(position, r)
        );

        holder.btnPagar.setOnClickListener(v ->
                listener.onPagar(position, r)
        );

        // Click en toda la tarjeta → ver detalle (opcional)
        holder.itemView.setOnClickListener(v ->
                listener.onVerDetalle(r)
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
