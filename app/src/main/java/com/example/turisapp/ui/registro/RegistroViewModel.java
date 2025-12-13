package com.example.turisapp.ui.registro;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.ActualizarUsuarioResponse;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensaje = new MutableLiveData<>();

    public RegistroViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    // ============================================================
    // Crear Usuario
    // ============================================================
    public void crearUsuario(String nombre,
                             String apellido,
                             String dni,
                             String telefono,
                             String email,
                             String clave,
                             String rol,
                             String estado) {

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || clave.isEmpty()) {
            mensaje.setValue("Complete todos los campos obligatorios");
            return;
        }

        ApiService api = ApiClient.getApiService();

        // 🔥 IMPORTANTE:
        // Si el usuario NO es administrador, NO enviamos token.
        String token = ApiClient.leerToken(getApplication());
        String auth = token != null ? "Bearer " + token : "";

        api.crearUsuario(
                auth,
                nombre,
                apellido,
                email,
                clave,
                rol,
                "",     // Avatar vacío por defecto
                estado,
                dni,
                telefono
        ).enqueue(new Callback<ActualizarUsuarioResponse>() {
            @Override
            public void onResponse(Call<ActualizarUsuarioResponse> call, Response<ActualizarUsuarioResponse> resp) {

                if (!resp.isSuccessful() || resp.body() == null) {
                    mensaje.setValue("Error al crear usuario");
                    return;
                }

                if (!"OK".equals(resp.body().getStatus())) {
                    mensaje.setValue("Error: " + resp.body().getMensaje());
                    return;
                }

                mensaje.setValue("Usuario creado correctamente");
            }

            @Override
            public void onFailure(Call<ActualizarUsuarioResponse> call, Throwable t) {
                mensaje.setValue("Fallo de conexión");
            }
        });
    }
}
