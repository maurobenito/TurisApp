package com.example.turisapp.modelo;

public class LoginResponse {
    private String status;
    private String token;
    private Usuario usuario;

    public String getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public Usuario getUsuario() {
        return usuario;
    }


}
