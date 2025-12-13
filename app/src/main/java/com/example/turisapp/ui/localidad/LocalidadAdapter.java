package com.example.turisapp.ui.localidad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turisapp.R;
import com.example.turisapp.modelo.Localidad;

import java.util.ArrayList;
import java.util.List;

public class LocalidadAdapter extends RecyclerView.Adapter<LocalidadAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(Localidad localidad);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Localidad localidad);
    }

    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    private List<Localidad> lista = new ArrayList<>();

    public void setLista(List<Localidad> nuevas) {
        this.lista = nuevas;
        notifyDataSetChanged();
    }


    public void setOnClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }


    @NonNull
    @Override
    public LocalidadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_localidad, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalidadAdapter.ViewHolder holder, int position) {
        Localidad loc = lista.get(position);

        holder.tvNombre.setText(loc.getNombre());


        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClick(loc);
        });


        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) longClickListener.onItemLongClick(loc);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreLocalidad);
        }
    }
}
