package com.example.turisapp.modelo;

import com.google.gson.annotations.SerializedName;

public class Reserva {

    @SerializedName("Id")
    private int id;

    @SerializedName("ClienteId")
    private int clienteId;

    @SerializedName("AlojamientoId")
    private int alojamientoId;

    @SerializedName("FechaInicio")
    private String fechaInicio;

    @SerializedName("FechaFin")
    private String fechaFin;

    @SerializedName("MontoTotal")
    private double montoTotal;

    @SerializedName("Estado")
    private String estado;

    @SerializedName("FechaCreacion")
    private String fechaCreacion;

    public Reserva(int id) {
        this.id = id;
    }
    @SerializedName("Alojamiento")
    private Alojamiento alojamiento;
    @SerializedName("AlojamientoNombre")
    private String alojamientoNombre;

    @SerializedName("AlojamientoTitulo")
    private String alojamientoTitulo;

    public String getAlojamientoTitulo() { return alojamientoTitulo; }

    public String getAlojamientoNombre() { return alojamientoNombre; }

    public Alojamiento getAlojamiento() { return alojamiento; }
    public void setAlojamiento(Alojamiento a) { this.alojamiento = a; }


    public Reserva(int id, int clienteId, int alojamientoId, String fechaInicio, String fechaFin, double montoTotal, String estado, String fechaCreacion) {
        this.id = id;
        this.clienteId = clienteId;
        this.alojamientoId = alojamientoId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getAlojamientoId() {
        return alojamientoId;
    }

    public void setAlojamientoId(int alojamientoId) {
        this.alojamientoId = alojamientoId;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y setters...
}
