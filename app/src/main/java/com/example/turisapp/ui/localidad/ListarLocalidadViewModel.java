package com.example.turisapp.ui.localidad;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.Localidad;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarLocalidadViewModel extends AndroidViewModel {


    private MutableLiveData<String> mensaje = new MutableLiveData<>();
    private MutableLiveData<Boolean> editar = new MutableLiveData<>(false);
    private MutableLiveData<Localidad> localidadSeleccionada = new MutableLiveData<>();

    public ListarLocalidadViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<List<Localidad>> getLocalidades() { return localidades; }
    public LiveData<String> getMensaje() { return mensaje; }
    public LiveData<Boolean> getEditar() { return editar; }
    public LiveData<Localidad> getLocalidadSeleccionada() { return localidadSeleccionada; }


    public  MutableLiveData<List<Localidad>> localidades = new MutableLiveData<>();

    public void cargarLocalidades() {

        ApiService api = ApiClient.getApiService();
        String token = "Bearer " + ApiClient.leerToken(getApplication());

        api.obtenerLocalidades(token).enqueue(new Callback<List<Localidad>>() {
            @Override
            public void onResponse(Call<List<Localidad>> call, Response<List<Localidad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    localidades.setValue(response.body());
                } else {
                    mensaje.setValue("Error al cargar localidades");
                }
            }

            @Override
            public void onFailure(Call<List<Localidad>> call, Throwable t) {
                mensaje.setValue("Error de conexión");
            }
        });
    }


    public void seleccionar(Localidad l) {
        localidadSeleccionada.setValue(l);
        editar.setValue(true);
    }

    public void nuevaLocalidad() {
        localidadSeleccionada.setValue(new Localidad());
        editar.setValue(false);
    }


    public void guardar(String nombre) {

        if (nombre.trim().isEmpty()) {
            mensaje.setValue("Ingrese un nombre");
            return;
        }

        Localidad l = localidadSeleccionada.getValue();

        if (l == null) return;

        l.setNombre(nombre);

        if (editar.getValue()) editarLocalidad(l);
        else crearLocalidad(l);
    }

    private void crearLocalidad(Localidad l) {

        ApiService api = ApiClient.getApiService();

        api.crearLocalidad(l.getNombre()).enqueue(new Callback<Localidad>() {
            @Override
            public void onResponse(Call<Localidad> call, Response<Localidad> response) {
                mensaje.setValue("Localidad creada");
                cargarLocalidades();
            }

            @Override
            public void onFailure(Call<Localidad> call, Throwable t) {
                mensaje.setValue("Error al crear");
            }
        });
    }

    private void editarLocalidad(Localidad l) {

        ApiService api = ApiClient.getApiService();

        api.editarLocalidad(l.getId(), l.getNombre()).enqueue(new Callback<Localidad>() {
            @Override
            public void onResponse(Call<Localidad> call, Response<Localidad> response) {
                mensaje.setValue("Localidad actualizada");
                cargarLocalidades();
            }

            @Override
            public void onFailure(Call<Localidad> call, Throwable t) {
                mensaje.setValue("Error al editar");
            }
        });
    }



    public void eliminar(Localidad l) {
        ApiService api = ApiClient.getApiService();

        api.eliminarLocalidad(l.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    mensaje.setValue("Localidad eliminada");

                    // 🔥 Punto clave: refrescar la lista después de eliminar
                    cargarLocalidades();
                } else {
                    mensaje.setValue("Error al eliminar");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mensaje.setValue("Error al eliminar");
            }
        });
    }


}
