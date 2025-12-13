package com.example.turisapp.modelo;

import java.io.Serializable;

public class Pago implements Serializable {

    private int id;
    private int reservaId;
    private String fechaPago;      // YYYY-MM-DD
    private double monto;
    private String medioDePago;    // Efectivo - Transferencia - Tarjeta - MercadoPago
    private String estadoPago;     // Pendiente - Pagado

    public Pago() {
    }

    public Pago(int id, int reservaId, String fechaPago, double monto, String medioDePago, String estadoPago) {
        this.id = id;
        this.reservaId = reservaId;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.medioDePago = medioDePago;
        this.estadoPago = estadoPago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservaId() {
        return reservaId;
    }

    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMedioDePago() {
        return medioDePago;
    }

    public void setMedioDePago(String medioDePago) {
        this.medioDePago = medioDePago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
}
