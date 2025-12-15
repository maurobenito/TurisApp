package com.example.turisapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    // -----------------------------
    // EVENTOS DE NAVEGACIÓN
    // -----------------------------
    private final MutableLiveData<Evento> evento = new MutableLiveData<>();

    public LiveData<Evento> getEvento() {
        return evento;
    }

    // -----------------------------
    // ACCIONES
    // -----------------------------
    public void irAlojamientos() {
        evento.setValue(Evento.ALOJAMIENTOS);
    }

    public void irLogin() {
        evento.setValue(Evento.LOGIN);
    }

    public void irRegistro() {
        evento.setValue(Evento.REGISTRO);
    }

    // -----------------------------
    // ENUM DE EVENTOS
    // -----------------------------
    public enum Evento {
        ALOJAMIENTOS,
        LOGIN,
        REGISTRO
    }
}
