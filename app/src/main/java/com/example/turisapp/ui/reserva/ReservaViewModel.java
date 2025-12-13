package com.example.turisapp.ui.reserva;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.Reserva;
import com.example.turisapp.modelo.ReservaResponse;
import com.example.turisapp.modelo.Alojamiento;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservaViewModel extends AndroidViewModel {


    public MutableLiveData<String> tituloAlojamiento = new MutableLiveData<>();
    public MutableLiveData<String> fechaInicioStr = new MutableLiveData<>();
    public MutableLiveData<String> fechaFinStr = new MutableLiveData<>();
    public MutableLiveData<Double> total = new MutableLiveData<>(0.0);
    public MutableLiveData<String> mensaje = new MutableLiveData<>();
    public MutableLiveData<Boolean> reservaExitosa = new MutableLiveData<>();


    private Alojamiento alojamiento;
    private Date fechaInicio;
    private Date fechaFin;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public ReservaViewModel(@NonNull Application app) {
        super(app);
    }


    public void iniciar(Alojamiento alo) {
        this.alojamiento = alo;
        tituloAlojamiento.setValue(alo.getTitulo());
    }


    public void seleccionarFechaInicio(Date fecha) {
        fechaInicio = fecha;
        fechaInicioStr.setValue(  sdf.format(fecha));

        mensaje.setValue(null);
        validarDisponibilidad();

        calcularTotal();
    }


    public void seleccionarFechaFin(Date fecha) {


        if (fechaInicio != null && !fecha.after(fechaInicio)) {
            fechaFin = null;
            fechaFinStr.setValue("Seleccionar fecha fin");
            total.setValue(0.0);
            mensaje.setValue("La fecha fin debe ser posterior a la fecha inicio");
            return;
        }


        fechaFin = fecha;
        fechaFinStr.setValue(sdf.format(fecha));

        mensaje.setValue(null);
        validarDisponibilidad();
        calcularTotal();
    }




    private void calcularTotal() {
        if (fechaInicio == null || fechaFin == null) return;

        long diff = fechaFin.getTime() - fechaInicio.getTime();
        if (diff <= 0) {
            total.setValue(0.0);
            return;
        }

        long dias = diff / (1000 * 60 * 60 * 24);
        double monto = dias * alojamiento.getPrecioPorDia();

        total.setValue(monto);
    }


    private void validarDisponibilidad() {

        // limpiar mensaje anterior
        mensaje.setValue(null);

        if (fechaInicio == null || fechaFin == null) return;

        String token = ApiClient.leerToken(getApplication());
        if (token == null) return;

        String auth = "Bearer " + token;
        ApiService api = ApiClient.getApiService();

        api.validarDisponibilidad(
                auth,
                alojamiento.getId(),
                fechaInicioStr.getValue(),
                fechaFinStr.getValue()
        ).enqueue(new Callback<ReservaResponse>() {
            @Override
            public void onResponse(Call<ReservaResponse> call, Response<ReservaResponse> resp) {

                if (!resp.isSuccessful() || resp.body() == null) return;

                ReservaResponse r = resp.body();

                if (!"OK".equals(r.getStatus())) {
                    mensaje.setValue(r.getMensaje());
                } else {
                    mensaje.setValue(null); // 👈 CLAVE
                }
            }

            @Override
            public void onFailure(Call<ReservaResponse> call, Throwable t) {
                // no mostrar nada
            }
        });
    }



            public void onFailure(Call<ReservaResponse> call, Throwable t) {
                // silencioso, no molestamos al usuario
            }


    public void confirmarReserva() {

        if (fechaInicio == null || fechaFin == null) {
            mensaje.setValue("Seleccione las fechas");
            return;
        }

        if (total.getValue() == null || total.getValue() <= 0) {
            mensaje.setValue("Las fechas no son válidas");
            return;
        }

        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            mensaje.setValue("Usuario no autenticado");
            return;
        }

        String auth = "Bearer " + token;

        ApiService api = ApiClient.getApiService();


        int clienteId = ApiClient.leerUsuarioId(getApplication());

        api.crearReserva(
                auth,
                clienteId,
                alojamiento.getId(),
                fechaInicioStr.getValue(),
                fechaFinStr.getValue(),
                total.getValue()
        ).enqueue(new Callback<ReservaResponse>() {
            @Override
            public void onResponse(Call<ReservaResponse> call, Response<ReservaResponse> resp) {

                if (!resp.isSuccessful() || resp.body() == null) {
                    mensaje.setValue("Error en el servidor");
                    return;
                }

                ReservaResponse r = resp.body();

                if (!"OK".equals(r.getStatus())) {
                    mensaje.setValue(r.getMensaje());
                    return;
                }
                mensaje.setValue("Reserva creada correctamente");
                reservaExitosa.setValue(true);
            }

            @Override
            public void onFailure(Call<ReservaResponse> call, Throwable t) {
                mensaje.setValue("Fallo en la conexión");
                Log.e("RESERVA_FAIL", t.getMessage());
            }
        });
    }
}
