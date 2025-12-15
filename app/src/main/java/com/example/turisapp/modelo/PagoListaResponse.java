package com.example.turisapp.modelo;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PagoListaResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("pagos")
    private List<Pago> pagos;

    public String getStatus() {
        return status;
    }

    public List<Pago> getPagos() {
        return pagos;
    }
}
