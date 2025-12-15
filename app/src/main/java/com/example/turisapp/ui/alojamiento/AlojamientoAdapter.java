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

public class AlojamientoAdapter
        extends RecyclerView.Adapter<AlojamientoAdapter.ViewHolderAlojamiento> {

    private List<Alojamiento> lista;
    private LayoutInflater inflater;

    public AlojamientoAdapter(List<Alojamiento> lista, Context context) {
        this.lista = lista;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderAlojamiento onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_alojamiento, parent, false);
        return new ViewHolderAlojamiento(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolderAlojamiento holder, int position) {

        Alojamiento aloj = lista.get(position);

        holder.titulo.setText(aloj.getTitulo());
        holder.precio.setText("$ " + aloj.getPrecioPorDia());

        // -------------------------------
        // IMAGEN (CAMBIO MÍNIMO)
        // -------------------------------
        String img = aloj.getImagenPrincipal();

        // ✅ solo esto es CLAVE
        Glide.with(holder.imagen).clear(holder.imagen);

        if (img == null || img.isEmpty()) {
            holder.imagen.setImageResource(R.drawable.placeholder);
        } else {
            Glide.with(holder.imagen)
                    .load(ApiClient.IMAGE_URL + img)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error_image)
                    .dontAnimate()
                    .into(holder.imagen);
        }

        // -------------------------------
        // CLICK → DETALLE (IGUAL QUE ANTES)
        // -------------------------------
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("alojamiento", aloj);

            NavController navController = Navigation.findNavController(v);
            navController.navigate(
                    R.id.detalleAlojamientoFragment,
                    bundle
            );
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    // VIEW HOLDER (SIN CAMBIOS)
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
