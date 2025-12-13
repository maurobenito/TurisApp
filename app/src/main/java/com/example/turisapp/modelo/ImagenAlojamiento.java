package com.example.turisapp.modelo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ImagenAlojamiento implements Serializable {

    @SerializedName("Id")
    private int id;

    @SerializedName("AlojamientoId")
    private int alojamientoId;

    @SerializedName("RutaImagen")
    private String rutaImagen;

    public int getId() {
        return id;
    }

    public int getAlojamientoId() {
        return alojamientoId;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }
}
