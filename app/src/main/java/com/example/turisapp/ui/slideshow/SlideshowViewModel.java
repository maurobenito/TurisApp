package com.example.turisapp.ui.slideshow;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SlideshowViewModel extends AndroidViewModel {

    // Notifica cuando el logout terminó
    private final MutableLiveData<Boolean> logoutOk = new MutableLiveData<>();

    public SlideshowViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getLogoutOk() {
        return logoutOk;
    }

    // --------------------------------------------------
    // LOGOUT
    // --------------------------------------------------
    public void cerrarSesion() {

        // 🔹 Borrar token
        SharedPreferences spToken =
                getApplication().getSharedPreferences("token.xml", 0);
        spToken.edit().clear().apply();

        // 🔹 Borrar datos usuario
        SharedPreferences spUsuario =
                getApplication().getSharedPreferences("usuario.xml", 0);
        spUsuario.edit().clear().apply();

        // 🔹 Notificar al Fragment
        logoutOk.setValue(true);
    }
}
