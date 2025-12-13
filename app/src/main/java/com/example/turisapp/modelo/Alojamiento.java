package com.example.turisapp.modelo;

import java.io.Serializable;

public class Alojamiento implements Serializable {

    private int id;
    private String titulo;
    private String tipo;                 // Cabaña, Departamento, Casa, Camping, Salón, Bungalow
    private String descripcion;
    private String direccion;
    private double precioPorDia;
    private int disponible;              // tinyint(1) → lo guardamos como int (0 o 1)
    private int cantidadHuespedes;
    private int habitaciones;
    private int pileta;                  // 0 / 1
    private int cochera;                 // 0 / 1
    private String estado;               // Activo / Inactivo
    private int localidadId;
    private int propietarioId;

    public Alojamiento(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    private String imagenPrincipal;

    public Alojamiento() {
    }


    public Alojamiento(String titulo, String tipo, String descripcion, String direccion,
                       double precioPorDia, int disponible, int cantidadHuespedes, int habitaciones,
                       int pileta, int cochera, String estado, int localidadId, int propietarioId,
                       String imagenPrincipal) {

        this.titulo = titulo;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.precioPorDia = precioPorDia;
        this.disponible = disponible;
        this.cantidadHuespedes = cantidadHuespedes;
        this.habitaciones = habitaciones;
        this.pileta = pileta;
        this.cochera = cochera;
        this.estado = estado;
        this.localidadId = localidadId;
        this.propietarioId = propietarioId;
        this.imagenPrincipal = imagenPrincipal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getPrecioPorDia() {
        return precioPorDia;
    }

    public void setPrecioPorDia(double precioPorDia) {
        this.precioPorDia = precioPorDia;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    public int getCantidadHuespedes() {
        return cantidadHuespedes;
    }

    public void setCantidadHuespedes(int cantidadHuespedes) {
        this.cantidadHuespedes = cantidadHuespedes;
    }

    public int getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(int habitaciones) {
        this.habitaciones = habitaciones;
    }

    public int getPileta() {
        return pileta;
    }

    public void setPileta(int pileta) {
        this.pileta = pileta;
    }

    public int getCochera() {
        return cochera;
    }

    public void setCochera(int cochera) {
        this.cochera = cochera;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getLocalidadId() {
        return localidadId;
    }

    public void setLocalidadId(int localidadId) {
        this.localidadId = localidadId;
    }

    public int getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(int propietarioId) {
        this.propietarioId = propietarioId;
    }
}
