package com.example.turisapp.ui.alojamiento;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.Alojamiento;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearAlojamientoViewModel extends AndroidViewModel {

    private MutableLiveData<String> mensaje = new MutableLiveData<>();

    public CrearAlojamientoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    // ============================================================
    // CREAR ALOJAMIENTO (Y SUBIR IMÁGENES)
    // ============================================================
    public void crearAlojamiento(Alojamiento a, List<Uri> imagenes) {

        // Validación mínima
        if (a.getTitulo().isEmpty() || a.getDireccion().isEmpty()) {
            mensaje.setValue("Complete los campos obligatorios");
            return;
        }

        ApiService api = ApiClient.getApiService();
        String token = ApiClient.leerToken(getApplication());
        String auth = token != null ? "Bearer " + token : "";

        // 1) CREAR ALOJAMIENTO
        api.crearAlojamiento(auth, a).enqueue(new Callback<Alojamiento>() {
            @Override
            public void onResponse(Call<Alojamiento> call, Response<Alojamiento> resp) {

                if (!resp.isSuccessful() || resp.body() == null) {
                    mensaje.setValue("Error al crear alojamiento");
                    return;
                }

                int alojamientoId = resp.body().getId();

                if (imagenes != null && !imagenes.isEmpty()) {
                    subirImagenes(alojamientoId, imagenes);
                }

                mensaje.setValue("Alojamiento creado correctamente");
            }

            @Override
            public void onFailure(Call<Alojamiento> call, Throwable t) {
                mensaje.setValue("Fallo de conexión");
            }
        });
    }

    // ============================================================
    // SUBIR TODAS LAS IMÁGENES
    // ============================================================
    private void subirImagenes(int alojamientoId, List<Uri> imagenes) {

        ApiService api = ApiClient.getApiService();
        String token = ApiClient.leerToken(getApplication());
        String auth = token != null ? "Bearer " + token : "";

        for (Uri uri : imagenes) {

            File archivo = convertirUriAFile(uri);
            if (archivo == null) continue;

            RequestBody rbImagen =
                    RequestBody.create(MediaType.parse("image/*"), archivo);

            MultipartBody.Part imagenPart =
                    MultipartBody.Part.createFormData("imagen", archivo.getName(), rbImagen);

            RequestBody rbId =
                    RequestBody.create(MediaType.parse("text/plain"), String.valueOf(alojamientoId));

            api.subirImagenAlojamiento(auth, imagenPart, rbId)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> resp) {
                            // NO mostramos mensaje por cada imagen
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            mensaje.postValue("Error subiendo imágenes");
                        }
                    });
        }
    }

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
