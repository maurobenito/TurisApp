package com.example.turisapp.ui.alojamiento;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turisapp.modelo.Alojamiento;
import com.example.turisapp.modelo.ImagenAlojamiento;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarAlojamientoViewModel extends AndroidViewModel {

    private final MutableLiveData<Alojamiento> alojamiento = new MutableLiveData<>();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    private final MutableLiveData<String> imagenPrincipal = new MutableLiveData<>();
    private final MutableLiveData<RecyclerView.Adapter<?>> imagenes = new MutableLiveData<>();
    private final MutableLiveData<ImagenAlojamiento> imagenAEliminar = new MutableLiveData<>();

    private final List<Uri> imagenesPendientes = new ArrayList<>();

    public EditarAlojamientoViewModel(@NonNull Application app) {
        super(app);
    }

    // =========================
    // GETTERS
    // =========================
    public LiveData<Alojamiento> getAlojamiento() {
        return alojamiento;
    }
    public LiveData<ImagenAlojamiento> getImagenAEliminar() {
        return imagenAEliminar;
    }


    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public LiveData<String> getImagenPrincipal() {
        return imagenPrincipal;
    }

    public LiveData<RecyclerView.Adapter<?>> getImagenes() {
        return imagenes;
    }

    // =========================
    // CARGAR ALOJAMIENTO
    // =========================
    public void cargarAlojamiento(Bundle bundle) {
        if (bundle == null) return;

        Alojamiento a = (Alojamiento) bundle.getSerializable("alojamiento");
        if (a == null) return;

        alojamiento.setValue(a);
        cargarImagenes(a.getId());
    }

    // =========================
    // CARGAR IMÁGENES
    // =========================
    private void cargarImagenes(int alojamientoId) {

        ApiService api = ApiClient.getApiService();

        api.listarImagenesPorAlojamiento(alojamientoId)
                .enqueue(new Callback<List<ImagenAlojamiento>>() {

                    @Override
                    public void onResponse(Call<List<ImagenAlojamiento>> call,
                                           Response<List<ImagenAlojamiento>> response) {

                        List<ImagenAlojamiento> lista = response.body();

                        if (lista == null || lista.isEmpty()) {
                            imagenPrincipal.setValue(null);
                            imagenes.setValue(null);
                            return;
                        }

                        imagenPrincipal.setValue(lista.get(0).getRutaImagen());

                        ImagenMiniAdapter adapter =
                                new ImagenMiniAdapter(
                                        lista,
                                        new ImagenMiniAdapter.OnImagenClick() {

                                            @Override
                                            public void onClick(String rutaImagen) {
                                                imagenPrincipal.setValue(rutaImagen);
                                            }

                                            @Override
                                            public void onLongClick(ImagenAlojamiento img) {
                                                eliminarImagen(img);
                                            }
                                        }
                                );

                        imagenes.setValue(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<ImagenAlojamiento>> call, Throwable t) {
                        imagenPrincipal.setValue(null);
                    }
                });
    }

    // =========================
    // RECIBIR IMÁGENES NUEVAS
    // =========================
    public void setImagenesSeleccionadas(List<Uri> uris) {
        imagenesPendientes.clear();
        if (uris != null) imagenesPendientes.addAll(uris);
        mensaje.setValue(imagenesPendientes.size() + " imagen(es) seleccionada(s)");
    }

    // =========================
    // ACTUALIZAR CAMPOS
    // =========================
    public void actualizarCampos(String titulo,
                                 String descripcion,
                                 String direccion,
                                 double precio,
                                 int huespedes,
                                 int habitaciones,
                                 boolean pileta,
                                 boolean cochera,
                                 boolean disponible) {

        Alojamiento a = alojamiento.getValue();
        if (a == null) return;

        a.setTitulo(titulo);
        a.setDescripcion(descripcion);
        a.setDireccion(direccion);
        a.setPrecioPorDia(precio);
        a.setCantidadHuespedes(huespedes);
        a.setHabitaciones(habitaciones);
        a.setPileta(pileta ? 1 : 0);
        a.setCochera(cochera ? 1 : 0);
        a.setDisponible(disponible ? 1 : 0);

        alojamiento.setValue(a);
    }

    // =========================
    // GUARDAR TODO
    // =========================
    public void guardarTodo() {

        Alojamiento a = alojamiento.getValue();
        if (a == null) return;

        ApiService api = ApiClient.getApiService();
        String auth = "Bearer " + ApiClient.leerToken(getApplication());

        api.actualizarAlojamiento(auth, a)
                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (!response.isSuccessful()) {
                            mensaje.setValue("Error al guardar");
                            return;
                        }

                        if (!imagenesPendientes.isEmpty()) {
                            subirImagenes(a.getId(), new ArrayList<>(imagenesPendientes));
                            imagenesPendientes.clear();
                        }

                        mensaje.setValue("Cambios guardados");
                        cargarImagenes(a.getId());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        mensaje.setValue("Error de conexión");
                    }
                });
    }

    // =========================
    // SUBIR IMÁGENES
    // =========================
    private void subirImagenes(int alojamientoId, List<Uri> imagenes) {

        ApiService api = ApiClient.getApiService();
        String auth = "Bearer " + ApiClient.leerToken(getApplication());

        for (Uri uri : imagenes) {

            File archivo = convertirUriAFile(uri);
            if (archivo == null) continue;

            RequestBody rbImagen =
                    RequestBody.create(MediaType.parse("image/*"), archivo);

            MultipartBody.Part imagenPart =
                    MultipartBody.Part.createFormData(
                            "imagen",
                            archivo.getName(),
                            rbImagen
                    );

            RequestBody rbId =
                    RequestBody.create(
                            MediaType.parse("text/plain"),
                            String.valueOf(alojamientoId)
                    );

            api.subirImagenAlojamiento(auth, imagenPart, rbId)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            cargarImagenes(alojamiento.getValue().getId()); // 🔥 refresca
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            mensaje.postValue("Error subiendo imágenes");
                        }
                    });
        }
    }

    // =========================
    // ELIMINAR IMAGEN
    // =========================
    private void eliminarImagen(ImagenAlojamiento img) {

        ApiService api = ApiClient.getApiService();
        String auth = "Bearer " + ApiClient.leerToken(getApplication());

        api.eliminarImagen(auth, img.getId())
                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            mensaje.setValue("Imagen eliminada");
                            cargarImagenes(alojamiento.getValue().getId());
                        } else {
                            mensaje.setValue("No se pudo eliminar la imagen");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        mensaje.setValue("Error al eliminar imagen");
                    }
                });
    }

    // =========================
    // UTILIDADES
    // =========================
    private File convertirUriAFile(Uri uri) {
        try {
            ContentResolver resolver = getApplication().getContentResolver();
            String nombre = obtenerNombreArchivo(uri);
            if (nombre == null) nombre = "imagen.jpg";

            File archivo = new File(getApplication().getCacheDir(), nombre);

            InputStream input = resolver.openInputStream(uri);
            FileOutputStream output = new FileOutputStream(archivo);

            byte[] buffer = new byte[4096];
            int bytes;
            while ((bytes = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytes);
            }

            input.close();
            output.close();
            return archivo;

        } catch (Exception e) {
            return null;
        }
    }

    private String obtenerNombreArchivo(Uri uri) {
        Cursor cursor = getApplication().getContentResolver()
                .query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            String nombre = cursor.getString(index);
            cursor.close();
            return nombre;
        }
        return null;
    }
}
