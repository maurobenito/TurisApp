package com.example.turisapp.ui.pago;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.GenericResponse;
import com.example.turisapp.modelo.Pago;
import com.example.turisapp.modelo.PagoListaResponse;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagoViewModel extends AndroidViewModel {

    private ApiService api;

    public MutableLiveData<List<Pago>> pagos = new MutableLiveData<>();
    public MutableLiveData<String> mensaje = new MutableLiveData<>();

    public PagoViewModel(@NonNull Application app) {
        super(app);
        api = ApiClient.getApiService();
    }

    // ==================================================
    // LISTAR PAGOS
    // ==================================================
    public void cargarPagos() {

        int usuarioId = ApiClient.leerUsuarioId(getApplication());
        String token = ApiClient.leerToken(getApplication());

        if (token == null) {
            mensaje.setValue("Usuario no autenticado");
            return;
        }

        String auth = "Bearer " + token;

        api.listarPagos(auth, usuarioId).enqueue(new Callback<PagoListaResponse>() {
            @Override
            public void onResponse(Call<PagoListaResponse> call,
                                   Response<PagoListaResponse> resp) {

                if (!resp.isSuccessful() || resp.body() == null) {
                    mensaje.setValue("Error al obtener pagos");
                    return;
                }

                if (!"OK".equals(resp.body().getStatus())) {
                    mensaje.setValue("Error al obtener pagos");

                    return;
                }

                pagos.setValue(resp.body().getPagos());
            }

            @Override
            public void onFailure(Call<PagoListaResponse> call, Throwable t) {
                mensaje.setValue("Fallo en la conexión");
            }
        });
    }

    // ==================================================
    // CONFIRMAR PAGO (PROPIETARIO)
    // ==================================================
    public void confirmarPago(String reservaId) {

        api.confirmarPago(Integer.parseInt(reservaId))
                .enqueue(new Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call,
                                           Response<GenericResponse> response) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && "OK".equals(response.body().getStatus())) {

                            mensaje.setValue("Pago confirmado");
                            cargarPagos();

                        } else {
                            mensaje.setValue("Error al confirmar el pago");
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        mensaje.setValue("Error de red al confirmar el pago");
                    }
                });
    }

    // ==================================================
    // RECHAZAR PAGO (PROPIETARIO)
    // ==================================================
    public void rechazarPago(String pagoId, String reservaId) {

        api.rechazarPago(
                Integer.parseInt(pagoId),
                Integer.parseInt(reservaId)
        ).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {

                if (response.isSuccessful()) {
                    mensaje.setValue("Pago rechazado");
                    cargarPagos();
                } else {
                    mensaje.setValue("Error al rechazar el pago");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mensaje.setValue("Error de red al rechazar el pago");
            }
        });
    }
}
