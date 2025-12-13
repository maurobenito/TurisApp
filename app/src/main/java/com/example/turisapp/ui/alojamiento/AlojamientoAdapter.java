package com.example.turisapp.ui.alojamiento;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.turisapp.R;
import com.example.turisapp.modelo.Alojamiento;
import com.example.turisapp.request.ApiClient;

import java.util.List;

public class AlojamientoAdapter extends RecyclerView.Adapter<AlojamientoAdapter.ViewHolderAlojamiento> {

    private List<Alojamiento> lista;
    private Context context;
    private LayoutInflater inflater;

    public AlojamientoAdapter(List<Alojamiento> lista, Context context, LayoutInflater inflater) {
        this.lista = lista;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolderAlojamiento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_alojamiento, parent, false);
        return new ViewHolderAlojamiento(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAlojamiento holder, int position) {

        Alojamiento aloj = lista.get(position);

        holder.titulo.setText(aloj.getTitulo());
        holder.precio.setText("$ " + aloj.getPrecioPorDia());

        Glide.with(context)
                .load(ApiClient.IMAGE_URL + aloj.getImagenPrincipal())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(holder.imagen);


        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("alojamiento", aloj);

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.detalleAlojamientoFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // 🔥 ACÁ ESTÁ EL VIEW HOLDER (dentro de la clase)
    public static class ViewHolderAlojamiento extends RecyclerView.ViewHolder {

        TextView titulo, precio;
        ImageView imagen;
        CardView card;

        public ViewHolderAlojamiento(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tvTituloAloj);
            precio = itemView.findViewById(R.id.tvPrecioAloj);
            imagen = itemView.findViewById(R.id.imgAloj);
            card = itemView.findViewById(R.id.cardAloj);
        }
    }
}
