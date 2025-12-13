package com.example.turisapp.modelo;

import java.util.List;

public class ReservaListaResponse {

    private String status;
    private List<Reserva> reservas;

    public String getStatus() {
        return status;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }
}
