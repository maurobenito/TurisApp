package com.example.turisapp.modelo;

public class ReservaResponse {
    private String status;
    private boolean success;
    private String mensaje;
    private Reserva reserva;
    public String getStatus() {
        return status;
    }
    public boolean isSuccess() {
        return success;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Reserva getReserva() {
        return reserva;
    }
}
