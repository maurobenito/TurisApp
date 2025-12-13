package com.example.turisapp.ui.perfil;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.turisapp.modelo.ActualizarUsuarioResponse;
import com.example.turisapp.modelo.PerfilResponse;
import com.example.turisapp.modelo.Usuario;
import com.example.turisapp.request.ApiClient;
import com.example.turisapp.request.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mEstado = new MutableLiveData<>(false);
    private final MutableLiveData<String> mNombre = new MutableLiveData<>("EDITAR");

    public PerfilViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<Usuario> getUsuario() { return usuario; }
    public LiveData<Boolean> getEstado() { return mEstado; }
    public LiveData<String> getNombreBoton() { return mNombre; }

    // =====================================================
    // OBTENER PERFIL
    // =====================================================
    public void obtenerPerfil() {

        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            Log.e("PERFIL", "Token NULL");
            return;
        }

        String auth = "Bearer " + token;

        ApiService api = ApiClient.getApiService();
        Call<PerfilResponse> call = api.obtenerUsuario(auth);

        call.enqueue(new Callback<PerfilResponse>() {
            @Override
            public void onResponse(Call<PerfilResponse> call, Response<PerfilResponse> resp) {

                if (!resp.isSuccessful() || resp.body() == null) {
                    Log.e("PERFIL_FAIL", "HTTP " + resp.code());
                    return;
                }

                PerfilResponse pr = resp.body();

                if (!"OK".equals(pr.getStatus())) {
                    Log.e("PERFIL_FAIL", "Mensaje: " + pr.getMensaje());
                    return;
                }

                usuario.postValue(pr.getUsuario());
                Log.d("PERFIL_OK", "Usuario recibido = " + pr.getUsuario().getNombre());
            }

            @Override
            public void onFailure(Call<PerfilResponse> call, Throwable t) {
                Log.e("PERFIL_FAIL", t.getMessage());
            }
        });
    }

    // =====================================================
    // BOTÓN EDITAR / GUARDAR
    // =====================================================
    public void cambioBoton(String boton, String nombre, String apellido, String dni,
                            String telefono, String email, String clave,
                            String rol, String estado) {

        if (boton.equalsIgnoreCase("EDITAR")) {
            mEstado.setValue(true);
            mNombre.setValue("GUARDAR");
            return;
        }

        Usuario u = usuario.getValue();
        if (u == null) return;

        // 🔥 SI CLAVE VIENE VACÍA → NO CAMBIARLA
        String claveFinal = clave.trim().isEmpty() ? null : clave;

        // NO TOCAMOS NADA RARO: copiamos tal cual todos los valores nuevos
        u.setNombre(nombre);
        u.setApellido(apellido);
        u.setDni(dni);              // AHORA SE GUARDA CORRECTAMENTE
        u.setTelefono(telefono);    // AHORA SE GUARDA CORRECTAMENTE
        u.setEmail(email);
        u.setRol(rol);
        u.setEstado(estado);
        u.setClave(claveFinal);     // sólo cambia si corresponde

        actualizarUsuario(u);
    }

    // =====================================================
    // ACTUALIZAR USUARIO
    // =====================================================
    private void actualizarUsuario(Usuario nuevo) {

        String token = ApiClient.leerToken(getApplication());
        String auth = "Bearer " + token;

        ApiService api = ApiClient.getApiService();

        api.actualizarUsuario(
                auth,
                nuevo.getId(),
                nuevo.getNombre(),
                nuevo.getApellido(),
                nuevo.getEmail(),
                nuevo.getClave(),   // null = NO modificar
                nuevo.getRol(),
                nuevo.getAvatar(),
                nuevo.getEstado(),
                nuevo.getDni(),         // ✔ AGREGADO
                nuevo.getTelefono()
        ).enqueue(new Callback<ActualizarUsuarioResponse>() {
            @Override
            public void onResponse(Call<ActualizarUsuarioResponse> call, Response<ActualizarUsuarioResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getApplication(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    return;
                }

                usuario.postValue(response.body().getUsuario());

                mEstado.postValue(false);
                mNombre.postValue("EDITAR");

                Toast.makeText(getApplication(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ActualizarUsuarioResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Fallo al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =====================================================
    // CONVERTIR URI → FILE REAL
    // =====================================================
    private File convertirUriAFile(Uri uri) {
        try {
            InputStream input = getApplication().getContentResolver().openInputStream(uri);
            File temp = File.createTempFile("avatar", ".jpg", getApplication().getCacheDir());

            OutputStream output = new FileOutputStream(temp);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            input.close();
            output.close();

            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =====================================================
    // SUBIR AVATAR
    // =====================================================
    public void actualizarAvatar(Uri uri) {
        if (uri == null) return;

        Usuario u = usuario.getValue();
        if (u == null) return;

        File file = convertirUriAFile(uri);
        if (file == null) {
            Toast.makeText(getApplication(), "Error al procesar imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = ApiClient.leerToken(getApplication());
        String auth = "Bearer " + token;

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part avatarPart =
                MultipartBody.Part.createFormData("avatar", file.getName(), reqFile);

        RequestBody idBody =
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(u.getId()));

        ApiService api = ApiClient.getApiService();

        api.subirAvatar(auth, avatarPart, idBody)
                .enqueue(new Callback<ActualizarUsuarioResponse>() {
                    @Override
                    public void onResponse(Call<ActualizarUsuarioResponse> call, Response<ActualizarUsuarioResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            usuario.postValue(resp.body().getUsuario());
                            Toast.makeText(getApplication(), "Avatar actualizado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), "Error al subir avatar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ActualizarUsuarioResponse> call, Throwable t) {
                        Toast.makeText(getApplication(), "Fallo conexión avatar", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // =====================================================
    // SPINNERS
    // =====================================================
    public int getIndexRol(String rol) {
        if (rol == null) return 0;
        switch (rol) {
            case "Propietario": return 1;
            case "Cliente": return 2;
            default: return 0;
        }
    }

    public int getIndexEstado(String e) {
        return "Inactivo".equals(e) ? 1 : 0;
    }
}
