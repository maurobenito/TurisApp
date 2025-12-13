package com.example.turisapp.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // ============================
    // URL DEL SERVIDOR
    // ============================
    public static final String BASE_URL = "http://192.168.100.7/turisapp/";

    public static final String IMAGE_URL = BASE_URL + "api/imagenes/";


    // ============================
    // TOKEN
    // ============================
    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        sp.edit().putString("token", token).apply();
    }

    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }

    // ============================
    // USUARIO ID
    // ============================
    public static void guardarUsuario(Context context, int idUsuario) {
        SharedPreferences sp = context.getSharedPreferences("usuario.xml", Context.MODE_PRIVATE);
        sp.edit().putInt("id", idUsuario).apply();
    }

    public static int leerUsuarioId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("usuario.xml", Context.MODE_PRIVATE);
        return sp.getInt("id", -1);
    }

    // ============================
    // ROL DEL USUARIO (NUEVO)
    // ============================
    public static void guardarRol(Context context, String rol) {
        SharedPreferences sp = context.getSharedPreferences("usuario.xml", Context.MODE_PRIVATE);
        sp.edit().putString("rol", rol).apply();
    }

    public static String leerRol(Context context) {
        SharedPreferences sp = context.getSharedPreferences("usuario.xml", Context.MODE_PRIVATE);
        return sp.getString("rol", "");
    }

    // ============================
    // RETROFIT
    // ============================
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {

        if (retrofit == null) {

            Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit.create(ApiService.class);
    }
}
