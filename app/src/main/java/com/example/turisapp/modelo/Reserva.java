package com.example.turisapp.modelo;

import com.google.gson.annotations.SerializedName;

public class Reserva {

    // =========================
    // DATOS BASE
    // =========================
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

    // =========================
    // DATOS DE ALOJAMIENTO
    // =========================
    @SerializedName("AlojamientoTitulo")
    private String alojamientoTitulo;

    @SerializedName("LocalidadNombre")
    private String localidadNombre;

    // (opcional para detalle)
    @SerializedName("Alojamiento")
    private Alojamiento alojamiento;

    // =========================
    // DATOS DE PERSONAS
    // =========================
    @SerializedName("ClienteNombre")
    private String clienteNombre;

    @SerializedName("ClienteApellido")
    private String clienteApellido;

    @SerializedName("PropietarioNombre")
    private String propietarioNombre;

    @SerializedName("PropietarioApellido")
    private String propietarioApellido;

    // =========================
    // CONSTRUCTORES
    // =========================
    public Reserva(int id) {
        this.id = id;
    }

    public Reserva(int id,
                   int clienteId,
                   int alojamientoId,
                   String fechaInicio,
                   String fechaFin,
                   double montoTotal,
                   String estado,
                   String fechaCreacion) {

        this.id = id;
        this.clienteId = clienteId;
        this.alojamientoId = alojamientoId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // =========================
    // GETTERS BASE
    // =========================
    public int getId() {
        return id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public int getAlojamientoId() {
        return alojamientoId;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    // =========================
    // GETTERS UI (CLAVE)
    // =========================
    public String getAlojamientoTitulo() {
        return alojamientoTitulo != null ? alojamientoTitulo : "";
    }

    public String getLocalidadNombre() {
        return localidadNombre != null ? localidadNombre : "";
    }

    public String getNombreCliente() {
        if (clienteNombre == null) return "";
        return clienteNombre + " " + (clienteApellido != null ? clienteApellido : "");
    }

    public String getNombrePropietario() {
        if (propietarioNombre == null) return "";
        return propietarioNombre + " " + (propietarioApellido != null ? propietarioApellido : "");
    }

    // =========================
    // ALOJAMIENTO COMPLETO (opcional)
    // =========================
    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }
}
