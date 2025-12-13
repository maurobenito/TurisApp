package com.example.turisapp.ui.alojamiento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.turisapp.R;
import com.example.turisapp.modelo.ImagenAlojamiento;
import com.example.turisapp.request.ApiClient;

import java.util.List;

public class ImagenMiniAdapter
        extends RecyclerView.Adapter<ImagenMiniAdapter.ViewHolder> {

    public interface OnImagenClick {
        void onClick(String rutaImagen);

        // 👇 NUEVO (opcional)
        default void onLongClick(ImagenAlojamiento img) {}
    }

    private final List<ImagenAlojamiento> lista;
    private final OnImagenClick listener;

    public ImagenMiniAdapter(List<ImagenAlojamiento> lista,
                             OnImagenClick listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_imagen_mini, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {

        ImagenAlojamiento img = lista.get(pos);

        Glide.with(h.itemView.getContext())
                .load(ApiClient.IMAGE_URL + img.getRutaImagen())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(h.imagen);


        h.itemView.setOnClickListener(v ->
                listener.onClick(img.getRutaImagen())
        );


        h.itemView.setOnLongClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar imagen")
                    .setMessage("¿Desea eliminar esta imagen?")
                    .setPositiveButton("Eliminar", (d, w) ->
                            listener.onLongClick(img) // ✅ SE ELIMINA SOLO ACÁ
                    )
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        ViewHolder(View v) {
            super(v);
            imagen = v.findViewById(R.id.imgMini);
        }
    }
}
