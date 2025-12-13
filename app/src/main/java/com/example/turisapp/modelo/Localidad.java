package com.example.turisapp.modelo;

import java.io.Serializable;

public class Localidad implements Serializable {

    private int id;
    private String nombre;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // 🔥 ESTA ES LA CLAVE
    @Override
    public String toString() {
        return nombre;   // Esto hace que el Spinner muestre el nombre
    }
}
