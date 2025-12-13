package com.example.turisapp.modelo;

import java.io.Serializable;

public class Documentacion implements Serializable {

    private int id;
    private int usuarioId;
    private String tipo;            // DNI / Contrato / Certificado / Otro
    private String nombreArchivo;
    private String rutaArchivo;
    private String fechaSubida;     // Se recibe como String del backend (datetime)
    private String observaciones;

    public Documentacion() {
    }

    public Documentacion(int id, int usuarioId, String tipo, String nombreArchivo,
                         String rutaArchivo, String fechaSubida, String observaciones) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
        this.fechaSubida = fechaSubida;
        this.observaciones = observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(String fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}


