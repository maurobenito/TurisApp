package com.example.turisapp.modelo;

public class PagoRequest {

    private int ReservaId;
    private double Monto;
    private String MedioDePago;
    private String EstadoPago;

    public PagoRequest(int reservaId, double monto, String medioDePago, String estadoPago) {
        this.ReservaId = reservaId;
        this.Monto = monto;
        this.MedioDePago = medioDePago;
        this.EstadoPago = estadoPago;
    }

    // getters y setters si los necesitás
}
