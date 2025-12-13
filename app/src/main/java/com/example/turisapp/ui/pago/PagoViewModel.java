package com.example.turisapp.ui.pago;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.Pago;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagoViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pago>> listaPagos = new MutableLiveData<>();

    public PagoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Pago>> getListaPagos() {
        return listaPagos;
    }

    public void obtenerPagos() {

        String token = ApiClient.leerToken(getApplication());
        ApiService api = ApiClient.getApiService();

        // 🔥 Igual que Alojamientos, pero llamando al endpoint de pagos del cliente
        Call<List<Pago>> call = api.obtenerPagosCliente(
                ApiClient.leerUsuarioId(getApplication())
        );

        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful()) {
                    listaPagos.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "No se pudieron obtener pagos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                Log.e("PagoVM", "Error: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }
}
