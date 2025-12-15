
package com.example.turisapp.ui.alojamiento;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turisapp.R;
import com.example.turisapp.modelo.Alojamiento;
import com.example.turisapp.modelo.ImagenAlojamiento;
import com.example.turisapp.modelo.Localidad;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleAlojamientoViewModel extends AndroidViewModel {

    private static final String TAG = "DETALLE_VM";


    public MutableLiveData<Alojamiento> alojamiento = new MutableLiveData<>();

    public MutableLiveData<String> localidadTexto = new MutableLiveData<>("Localidad");
    public MutableLiveData<String> botonTexto = new MutableLiveData<>("Acción");
    public MutableLiveData<Integer> botonColor =
            new MutableLiveData<>(android.R.color.holo_green_dark);

    public MutableLiveData<Integer> navActionId = new MutableLiveData<>();
    public MutableLiveData<String> tituloPantalla = new MutableLiveData<>();
    public MutableLiveData<String> mensaje = new MutableLiveData<>();

    // =========================
    // GALERÍA (lo que consume el Fragment)
    // =========================
    private final MutableLiveData<String> imagenPrincipal =
            new MutableLiveData<>("placeholder.jpg"); // evita imagen negra

    private final MutableLiveData<RecyclerView.Adapter<?>> imagenes =
            new MutableLiveData<>();

    public LiveData<String> imagenPrincipal() {
        return imagenPrincipal;
    }

    public LiveData<RecyclerView.Adapter<?>> imagenes() {
        return imagenes;
    }

    public DetalleAlojamientoViewModel(@NonNull Application app) {
        super(app);
        Log.d(TAG, "ViewModel creado");
    }


    public void cargarAlojamiento(Bundle bundle) {
        try {
            Alojamiento a = (Alojamiento) bundle.getSerializable("alojamiento");
            if (a == null) {
                Log.e(TAG, "Alojamiento NULL en bundle");
                return;
            }

            Log.d(TAG, "Alojamiento cargado id=" + a.getId());

            alojamiento.setValue(a);
            tituloPantalla.setValue(a.getTitulo());

            configurarBotonSegunRol();
            cargarLocalidadNombre();
            cargarImagenes(a.getId());

        } catch (Exception e) {
            Log.e(TAG, "Error bundle", e);
        }
    }


    private void cargarImagenes(int alojamientoId) {

        Log.d(TAG, "Solicitando imágenes para alojamientoId=" + alojamientoId);

        ApiService api = ApiClient.getApiService();

        api.listarImagenesPorAlojamiento(alojamientoId)
                .enqueue(new Callback<List<ImagenAlojamiento>>() {
                    @Override
                    public void onResponse(Call<List<ImagenAlojamiento>> call,
                                           Response<List<ImagenAlojamiento>> response) {

                        if (!response.isSuccessful()) {
                            Log.e(TAG, "Error HTTP imágenes: " + response.code());
                            return;
                        }

                        List<ImagenAlojamiento> lista = response.body();

                        Log.d(TAG, "Imágenes recibidas = " +
                                (lista == null ? "null" : lista.size()));

                        if (lista == null || lista.isEmpty()) {
                            Log.e(TAG, "Lista de imágenes VACÍA");
                            return;
                        }

                        String ruta = lista.get(0).getRutaImagen();
                        Log.d(TAG, "Imagen principal = " + ruta);

                        imagenPrincipal.setValue(ruta);

                        ImagenMiniAdapter adapter =
                                new ImagenMiniAdapter(lista,
                                        r -> {
                                            Log.d(TAG, "Miniatura click = " + r);
                                            imagenPrincipal.setValue(r);
                                        });

                        imagenes.setValue(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<ImagenAlojamiento>> call, Throwable t) {
                        Log.e(TAG, "Fallo al cargar imágenes", t);
                    }
                });
    }


    private void configurarBotonSegunRol() {

        String rol = ApiClient.leerRol(getApplication());
        rol = rol == null ? "" : rol;

        boolean esCliente = "Cliente".equalsIgnoreCase(rol);

        botonTexto.setValue(
                esCliente ? "Buscar fechas disponibles" : "Editar alojamiento"
        );

        botonColor.setValue(
                esCliente
                        ? android.R.color.holo_green_dark
                        : android.R.color.holo_blue_dark
        );

        Log.d(TAG, "Rol=" + rol + " esCliente=" + esCliente);
    }

    public void onAccionClick() {

        String rol = ApiClient.leerRol(getApplication());
        rol = rol == null ? "" : rol;

        boolean esCliente = "Cliente".equalsIgnoreCase(rol);

        int action = esCliente
                ? R.id.reservaFragment
                : R.id.action_detalleAlojamientoFragment_to_editarAlojamientoFragment;

        Log.d(TAG, "Navegando actionId=" + action);
        navActionId.setValue(action);
    }


    private void cargarLocalidadNombre() {

        ApiService api = ApiClient.getApiService();
        String token = ApiClient.leerToken(getApplication());
        String auth = token != null ? "Bearer " + token : "";

        api.obtenerLocalidades(auth).enqueue(new Callback<List<Localidad>>() {
            @Override
            public void onResponse(Call<List<Localidad>> call,
                                   Response<List<Localidad>> response) {

                List<Localidad> lista = response.body();
                if (lista == null) return;

                Alojamiento a = alojamiento.getValue();
                if (a == null) return;

                for (Localidad l : lista) {
                    if (l.getId() == a.getLocalidadId()) {
                        localidadTexto.setValue(l.getNombre());
                        Log.d(TAG, "Localidad = " + l.getNombre());
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Localidad>> call, Throwable t) {
                Log.e(TAG, "Error localidades", t);
            }
        });

    }

}
