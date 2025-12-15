package com.example.turisapp.modelo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Pago implements Serializable {

    @SerializedName("Id")
    private String id;

    @SerializedName("ReservaId")
    private String reservaId;

    @SerializedName("FechaPago")
    private String fechaPago;

    @SerializedName("Monto")
    private String monto;

    @SerializedName("MedioDePago")
    private String medioDePago;

    @SerializedName("EstadoPago")
    private String estadoPago;

    // =====================================
    // 🔹 CAMPOS EXTRA (JOINs)
    // =====================================
    @SerializedName("AlojamientoTitulo")
    private String alojamientoTitulo;

    @SerializedName("ClienteNombre")
    private String clienteNombre;

    @SerializedName("ClienteApellido")
    private String clienteApellido;

    @SerializedName("PropietarioNombre")
    private String propietarioNombre;

    @SerializedName("PropietarioApellido")
    private String propietarioApellido;
    @SerializedName("FechaInicio")
    private String fechaInicio;

    @SerializedName("FechaFin")
    private String fechaFin;

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public Pago() {
    }

    // =====================================
    // GETTERS EXISTENTES
    // =====================================
    public String getId() {
        return id;
    }

    public String getReservaId() {
        return reservaId;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public String getMonto() {
        return monto;
    }

    public String getMedioDePago() {
        return medioDePago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    // =====================================
    // GETTERS NUEVOS (MINIMOS)
    // =====================================
    public String getAlojamientoTitulo() {
        return alojamientoTitulo;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public String getClienteApellido() {
        return clienteApellido;
    }

    public String getPropietarioNombre() {
        return propietarioNombre;
    }

    public String getPropietarioApellido() {
        return propietarioApellido;
    }
}
