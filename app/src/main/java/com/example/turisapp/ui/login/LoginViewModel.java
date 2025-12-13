package com.example.turisapp.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.LoginResponse;
import com.example.turisapp.modelo.Usuario;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<Usuario> loginOK = new MutableLiveData<>();
    private MutableLiveData<String> loginError = new MutableLiveData<>();
    private MutableLiveData<String> tokenOK = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Usuario> getLoginOK() {
        return loginOK;
    }

    public MutableLiveData<String> getLoginError() {
        return loginError;
    }

    public MutableLiveData<String> getTokenOK() {
        return tokenOK;
    }

    public void login(String email, String clave) {

        if (email.isEmpty() || clave.isEmpty()) {
            loginError.setValue("Faltan datos");
            return;
        }

        ApiService api = ApiClient.getApiService();
        Call<LoginResponse> call = api.login(email, clave);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (!response.isSuccessful()) {
                    loginError.setValue("Error en el servidor");
                    return;
                }

                LoginResponse body = response.body();
                if (body == null || !"OK".equals(body.getStatus())) {
                    loginError.setValue("Credenciales incorrectas");
                    return;
                }

                // ==============================
                // GUARDAR TOKEN
                // ==============================
                ApiClient.guardarToken(getApplication(), body.getToken());

                // ==============================
                // GUARDAR ID DEL USUARIO
                // ==============================
                getApplication()
                        .getSharedPreferences("usuario.xml", 0)
                        .edit()
                        .putInt("id", body.getUsuario().getId())
                        .apply();

                // ==============================
                // 🚀 GUARDAR EL ROL DEL USUARIO
                // ==============================
                ApiClient.guardarRol(getApplication(), body.getUsuario().getRol());

                // Mandamos datos al fragment
                tokenOK.setValue(body.getToken());
                loginOK.setValue(body.getUsuario());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginError.setValue("Error: " + t.getMessage());
            }
        });
    }
}
