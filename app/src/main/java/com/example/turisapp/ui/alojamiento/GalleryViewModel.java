package com.example.turisapp.ui.alojamiento;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.Alojamiento;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryViewModel extends AndroidViewModel {

    private MutableLiveData<List<Alojamiento>> listaAlojamientos = new MutableLiveData<>();

    public GalleryViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Alojamiento>> getListaAlojamientos() {
        return listaAlojamientos;
    }

    public void obtenerAlojamientos() {
        String token = ApiClient.leerToken(getApplication());
        ApiService api = ApiClient.getApiService();

        Call<List<Alojamiento>> call = api.obtenerAlojamientos("Bearer " + token);

        call.enqueue(new Callback<List<Alojamiento>>() {
            @Override
            public void onResponse(Call<List<Alojamiento>> call, Response<List<Alojamiento>> response) {
                if (response.isSuccessful()) {
                    listaAlojamientos.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "No se pudieron obtener alojamientos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Alojamiento>> call, Throwable t) {
                Log.e("AlojamientoVM", "Error: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de conexión", Toast.LENGTH_LONG).show();
            }
        });
    }
}
