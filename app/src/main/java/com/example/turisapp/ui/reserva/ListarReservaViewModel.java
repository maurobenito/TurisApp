package com.example.turisapp.ui.reserva;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.PagoResponse;
import com.example.turisapp.modelo.Reserva;
import com.example.turisapp.modelo.ReservaListaResponse;
import com.example.turisapp.modelo.CancelarResponse;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;
import com.example.turisapp.modelo.PagoRequest;



import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarReservaViewModel extends AndroidViewModel {

    public MutableLiveData<List<Reserva>> reservas = new MutableLiveData<>();
    public MutableLiveData<String> mensaje = new MutableLiveData<>();
    public MutableLiveData<Boolean> cancelacionExitosa = new MutableLiveData<>();

    public ListarReservaViewModel(@NonNull Application app) {
        super(app);
    }

    // -------------------------------------------------------------
    // CARGAR RESERVAS
    // -------------------------------------------------------------
    public void cargarReservas() {

        int clienteId = ApiClient.leerUsuarioId(getApplication());
        String token = ApiClient.leerToken(getApplication());

        if (token == null) {
            mensaje.setValue("Usuario no autenticado");
            return;
        }

        String auth = "Bearer " + token;

        ApiService api = ApiClient.getApiService();

        api.listarReservas(auth, clienteId).enqueue(new Callback<ReservaListaResponse>() {
            @Override
            public void onResponse(Call<ReservaListaResponse> call, Response<ReservaListaResponse> resp) {

                if (!resp.isSuccessful() || resp.body() == null) {
                    mensaje.setValue("Error al obtener reservas");
                    return;
                }

                if (!"OK".equals(resp.body().getStatus())) {
                    mensaje.setValue("Error: " + resp.body().getStatus());
                    return;
                }

                reservas.setValue(resp.body().getReservas());
            }

            @Override
            public void onFailure(Call<ReservaListaResponse> call, Throwable t) {
                mensaje.setValue("Fallo en la conexión");
            }
        });
    }

    // -------------------------------------------------------------
    // CANCELAR RESERVA
    // -------------------------------------------------------------
    public void cancelarReserva(Reserva r) {

        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            mensaje.setValue("Usuario no autenticado");
            return;
        }

        String auth = "Bearer " + token;

        ApiService api = ApiClient.getApiService();

        api.cancelarReserva(auth, r.getId()).enqueue(new Callback<CancelarResponse>() {
            @Override
            public void onResponse(Call<CancelarResponse> call, Response<CancelarResponse> resp) {

                if (!resp.isSuccessful() || resp.body() == null) {
                    mensaje.setValue("No se pudo cancelar la reserva");
                    return;
                }

                if (!"OK".equals(resp.body().getStatus())) {
                    mensaje.setValue(resp.body().getMensaje());
                    return;
                }

                mensaje.setValue("Reserva cancelada");
                cancelacionExitosa.setValue(true);
            }

            @Override
            public void onFailure(Call<CancelarResponse> call, Throwable t) {
                mensaje.setValue("Fallo al cancelar");
            }
        });
    }
    // -------------------------------------------------------------
// REGISTRAR PAGO
// -------------------------------------------------------------
    public void registrarPago(int reservaId, double monto, String medio) {

        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            mensaje.setValue("Usuario no autenticado");
            return;
        }

        String auth = "Bearer " + token;

        ApiService api = ApiClient.getApiService();

        PagoRequest pago = new PagoRequest(reservaId, monto, medio, "Pagado");

        api.crearPago(auth, pago).enqueue(new Callback<PagoResponse>() {
            @Override
            public void onResponse(Call<PagoResponse> call, Response<PagoResponse> resp) {

                if (!resp.isSuccessful() || resp.body() == null) {
                    mensaje.setValue("Error al registrar pago");
                    return;
                }

                if (!"OK".equals(resp.body().getMensaje())) {
                    mensaje.setValue(resp.body().getMensaje());
                    return;
                }

                mensaje.setValue("Pago registrado correctamente");
                cancelacionExitosa.setValue(true); // reutilizamos para refrescar la lista
            }

            @Override
            public void onFailure(Call<PagoResponse> call, Throwable t) {
                mensaje.setValue("Fallo en registrar pago");
            }
        });
    }

}
