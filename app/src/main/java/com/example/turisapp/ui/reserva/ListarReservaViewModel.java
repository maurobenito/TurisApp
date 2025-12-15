package com.example.turisapp.ui.reserva;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.GenericResponse;
import com.example.turisapp.modelo.Reserva;
import com.example.turisapp.modelo.ReservaListaResponse;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarReservaViewModel extends AndroidViewModel {

    private ApiService api;

    public MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();
    public MutableLiveData<String> mensaje = new MutableLiveData<>();

    public ListarReservaViewModel(@NonNull Application application) {
        super(application);
        api = ApiClient.getApiService();
    }

    // ==================================================
    // CONFIRMAR RESERVA  (Propietario)
    // Estado: PendienteConfirmacion → PendientePago
    // ==================================================
    public void confirmarReserva(int reservaId) {

        api.confirmarReserva(reservaId).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && "OK".equals(response.body().getStatus())) {

                    mensaje.setValue("Reserva confirmada");
                    cargarReservas();

                } else {
                    mensaje.setValue("Error al confirmar la reserva");
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                mensaje.setValue("Error de red al confirmar la reserva");
            }
        });
    }

    // ==================================================
    // RECHAZAR RESERVA  (Propietario)
    // Estado: PendienteConfirmacion → Rechazada
    // ==================================================
    public void rechazarReserva(int reservaId) {

        api.rechazarReserva(reservaId).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && "OK".equals(response.body().getStatus())) {

                    mensaje.setValue("Reserva rechazada");
                    cargarReservas();

                } else {
                    mensaje.setValue("Error al rechazar la reserva");
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                mensaje.setValue("Error de red al rechazar la reserva");
            }
        });
    }

    // ==================================================
    // CANCELAR RESERVA  (Cliente / Propietario)
    // Estado: PendienteConfirmacion / PendientePago → Cancelada
    // ==================================================
    public void cancelarReserva(int reservaId) {

        // ⚠️ este endpoint usa "Id", no "ReservaId"
        api.cancelarReserva(reservaId).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && "OK".equals(response.body().getStatus())) {

                    mensaje.setValue("Reserva cancelada");
                    cargarReservas();

                } else {
                    mensaje.setValue("Error al cancelar la reserva");
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                mensaje.setValue("Error de red al cancelar la reserva");
            }
        });
    }

    // ==================================================
// REGISTRAR PAGO  (Cliente)
// Crea pago en estado Pendiente
// ==================================================
    public void registrarPago(int reservaId, double monto, String medio) {

        api.registrarPago(reservaId, monto, medio).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && "OK".equals(response.body().getStatus())) {

                    mensaje.setValue("Pago registrado. Esperando confirmación");
                    cargarReservas();

                } else {
                    mensaje.setValue("Error al registrar el pago");
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                mensaje.setValue("Error de red al registrar el pago");
            }
        });
    }
    // ==================================================
    // CONFIRMAR PAGO  (Propietario)
    // Estado: PendientePago → Pagada
    // ==================================================
    public void confirmarPago(int reservaId) {

        api.confirmarPago(reservaId).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call,
                                   Response<GenericResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && "OK".equals(response.body().getStatus())) {

                    mensaje.setValue("Pago confirmado");
                    cargarReservas();

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
    // LISTAR RESERVAS
    // ==================================================
    public void cargarReservas() {

        String token = ApiClient.leerToken(getApplication());
        int usuarioId = ApiClient.leerUsuarioId(getApplication());

        // 👇 LOG DE DEBUG
        Log.d("RESERVAS", "Token=" + token + " UsuarioId=" + usuarioId);

        if (token == null || token.isEmpty() || usuarioId <= 0) {
            mensaje.setValue("Usuario no válido");
            return;
        }

        api.listarReservas(
                "Bearer " + token,
                usuarioId
        ).enqueue(new Callback<ReservaListaResponse>() {

            @Override
            public void onResponse(Call<ReservaListaResponse> call,
                                   Response<ReservaListaResponse> resp) {

                Log.d("RESERVAS", "HTTP=" + resp.code());

                if (!resp.isSuccessful() || resp.body() == null) {
                    mensaje.setValue("Error al obtener reservas");
                    return;
                }

                Log.d("RESERVAS", "Reservas=" + resp.body().getReservas().size());
                reservas.setValue(resp.body().getReservas());
            }

            @Override
            public void onFailure(Call<ReservaListaResponse> call, Throwable t) {
                Log.e("RESERVAS", "Fallo", t);
                mensaje.setValue("Fallo en la conexión");
            }
        });
    }


}
