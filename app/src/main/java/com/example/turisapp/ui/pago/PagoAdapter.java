package com.example.turisapp.ui.pago;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turisapp.R;
import com.example.turisapp.modelo.Pago;

import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.ViewHolderPago> {

    private List<Pago> lista;
    private Context context;
    private LayoutInflater inflater;

    public PagoAdapter(List<Pago> lista, Context context, LayoutInflater inflater) {
        this.lista = lista;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolderPago onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_pago, parent, false);
        return new ViewHolderPago(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPago holder, int position) {
        Pago p = lista.get(position);

        holder.monto.setText("Monto: $" + p.getMonto());
        holder.fecha.setText("Fecha: " + p.getFechaPago());
        holder.medio.setText("Medio: " + p.getMedioDePago());
        holder.estado.setText("Estado: " + p.getEstadoPago());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolderPago extends RecyclerView.ViewHolder {

        TextView monto, fecha, medio, estado;

        public ViewHolderPago(@NonNull View itemView) {
            super(itemView);
            monto = itemView.findViewById(R.id.tvMonto);
            fecha = itemView.findViewById(R.id.tvFecha);
            medio = itemView.findViewById(R.id.tvMedio);
            estado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
