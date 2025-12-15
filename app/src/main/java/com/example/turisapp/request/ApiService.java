package com.example.turisapp.request;


import com.example.turisapp.modelo.ActualizarUsuarioResponse;
import com.example.turisapp.modelo.Alojamiento;
import com.example.turisapp.modelo.CancelarResponse;
import com.example.turisapp.modelo.Documentacion;
import com.example.turisapp.modelo.GenericResponse;
import com.example.turisapp.modelo.ImagenAlojamiento;
import com.example.turisapp.modelo.Localidad;
import com.example.turisapp.modelo.LoginResponse;
import com.example.turisapp.modelo.NotificacionListaResponse;
import com.example.turisapp.modelo.Pago;
import com.example.turisapp.modelo.PagoListaResponse;
import com.example.turisapp.modelo.PagoRequest;
import com.example.turisapp.modelo.PagoResponse;
import com.example.turisapp.modelo.PerfilResponse;
import com.example.turisapp.modelo.Reserva;
import com.example.turisapp.modelo.ReservaListaResponse;
import com.example.turisapp.modelo.ReservaResponse;
import com.example.turisapp.modelo.UsuarioListaResponse;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // =====================================================
    // LOGIN
    // =====================================================
    @FormUrlEncoded
    @POST("api/login.php")
    Call<LoginResponse> login(
            @Field("Email") String email,
            @Field("Clave") String clave
    );

    // =====================================================
    // OBTENER USUARIO POR TOKEN
    // (tu PHP devuelve un Usuario directo)
    // =====================================================
// =====================================================
// OBTENER USUARIO POR TOKEN
// =====================================================
    @GET("api/usuarios/obtenerUsuario.php")
    Call<PerfilResponse> obtenerUsuario(
            @Header("Authorization") String token
    );

    // =====================================================
    // ACTUALIZAR PERFIL (FORM POST)
    // =====================================================
    @FormUrlEncoded
    @POST("api/usuarios/actualizarUsuario.php")
    Call<ActualizarUsuarioResponse> actualizarUsuario(
            @Header("Authorization") String token,
            @Field("Id") int id,
            @Field("Nombre") String nombre,
            @Field("Apellido") String apellido,
            @Field("Email") String email,
            @Field("Clave") String clave,
            @Field("Rol") String rol,
            @Field("Avatar") String avatar,
            @Field("Estado") String estado,
            @Field("Dni") String dni,
            @Field("Telefono") String telefono
    );

    // =====================================================
    // SUBIR AVATAR
    // =====================================================
    @Multipart
    @POST("api/usuarios/subir-avatar.php")
    Call<ActualizarUsuarioResponse> subirAvatar(
            @Header("Authorization") String token,
            @Part MultipartBody.Part avatar,
            @Part("UsuarioId") RequestBody userId
    );

    // =====================================================
    // SUBIR DOCUMENTO
    // =====================================================
    @Multipart
    @POST("api/usuarios/subir-documento.php")
    Call<Documentacion> subirDocumento(
            @Header("Authorization") String token,
            @Part MultipartBody.Part archivo,
            @Part("UsuarioId") RequestBody usuarioId,
            @Part("Tipo") RequestBody tipo,
            @Part("Observaciones") RequestBody observaciones
    );

    @FormUrlEncoded
    @POST("api/usuarios/crearUsuario.php")
    Call<ActualizarUsuarioResponse> crearUsuario(
            @Header("Authorization") String token,
            @Field("Nombre") String nombre,
            @Field("Apellido") String apellido,
            @Field("Email") String email,
            @Field("Clave") String clave,
            @Field("Rol") String rol,
            @Field("Avatar") String avatar,
            @Field("Estado") String estado,
            @Field("Dni") String dni,
            @Field("Telefono") String telefono
    );


    // =====================================================
    // ALOJAMIENTOS
    // =====================================================
    @GET("api/alojamiento/obtenerTodos.php")
    Call<List<Alojamiento>> obtenerAlojamientos(
            @Header("Authorization") String token,
            @Query("Rol") String rol,
            @Query("UsuarioId") int usuarioId
    );

    @GET("api/alojamiento/{id}")
    Call<Alojamiento> obtenerAlojamientoPorId(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    // =====================================================
// CREAR ALOJAMIENTO (JSON)
// =====================================================
    @POST("api/alojamiento/crear.php")
    Call<Alojamiento> crearAlojamiento(
            @Header("Authorization") String token,
            @Body Alojamiento alojamiento
    );

    // =====================================================
// SUBIR IMAGEN DE ALOJAMIENTO (MULTIPART)
// =====================================================
    @Multipart
    @POST("api/alojamiento/subirImagen.php")
    Call<Void> subirImagenAlojamiento(
            @Header("Authorization") String token,
            @Part MultipartBody.Part imagen,
            @Part("AlojamientoId") RequestBody alojamientoId
    );

    @GET("api/alojamiento/listarPorAlojamiento.php")
    Call<List<ImagenAlojamiento>> listarImagenesPorAlojamiento(
            @Query("AlojamientoId") int alojamientoId
    );

    @FormUrlEncoded
    @POST("api/alojamiento/eliminarImagen.php")
    Call<Void> eliminarImagen(
            @Header("Authorization") String token,
            @Field("Id") int imagenId
    );

    @FormUrlEncoded
    @POST("api/reserva/validarDisponibilidad.php")
    Call<ReservaResponse> validarDisponibilidad(
            @Header("Authorization") String token,
            @Field("AlojamientoId") int alojamientoId,
            @Field("FechaInicio") String fechaInicio,
            @Field("FechaFin") String fechaFin
    );

    // =====================================================
// SETEAR IMAGEN PRINCIPAL
// =====================================================
    @FormUrlEncoded
    @POST("api/imagenes/setPrincipal.php")
    Call<Void> setPrincipal(
            @Field("ImagenId") int imagenId,
            @Field("AlojamientoId") int alojamientoId
    );

    @PUT("api/alojamiento/actualizar.php")
    Call<Void> actualizarAlojamiento(
            @Header("Authorization") String token,
            @Body Alojamiento alojamiento
    );

    @POST("api/alojamiento/eliminar.php")
    Call<Void> eliminarAlojamiento(
            @Header("Authorization") String token,
            @Query("Id") int alojamientoId
    );



    // =====================================================
    // LOCALIDADES
    // =====================================================

    // CREAR LOCALIDAD
    @FormUrlEncoded
    @POST("api/localidades/crear.php")
    Call<Localidad> crearLocalidad(
            @Field("nombre") String nombre
    );

    @GET("api/localidades/listar.php")
    Call<List<Localidad>> obtenerLocalidades(@Header("Authorization") String token);

    // EDITAR LOCALIDAD
    @FormUrlEncoded
    @POST("api/localidades/editar.php")
    Call<Localidad> editarLocalidad(
            @Field("id") int id,
            @Field("nombre") String nombre
    );


    // ELIMINAR LOCALIDAD
    @FormUrlEncoded
    @POST("api/localidades/eliminar.php")
    Call<String> eliminarLocalidad(
            @Field("id") int id
    );
    // =====================================================
    // RESERVAS
    // =====================================================


    @FormUrlEncoded
    @POST("api/reserva/crearReserva.php")
    Call<ReservaResponse> crearReserva(
            @Header("Authorization") String token,
            @Field("ClienteId") int clienteId,
            @Field("AlojamientoId") int alojamientoId,
            @Field("FechaInicio") String fechaInicio,
            @Field("FechaFin") String fechaFin,
            @Field("MontoTotal") double montoTotal
    );


    @FormUrlEncoded
    @POST("api/reserva/cancelarReserva.php")
    Call<CancelarResponse> cancelarReserva(
            @Header("Authorization") String token,
            @Field("Id") int reservaId
    );


    @GET("api/reserva/listarPorUsuario.php")
    Call<ReservaListaResponse> listarReservas(
            @Header("Authorization") String token,
            @Query("UsuarioId") int usuarioId
    );


    // =====================================================
    // PAGOS
    // =====================================================
    @POST("api/pago/crear.php")
    Call<PagoResponse> crearPago(
            @Header("Authorization") String token,
            @Body PagoRequest pago
    );

    @GET("api/pago/pagosPorCliente.php")
    Call<List<Pago>> obtenerPagosCliente(
            @Header("Authorization") String token
    );


    @GET("api/pago/pagosPorPropietario.php")
    Call<List<Pago>> obtenerPagosPropietario(
            @Header("Authorization") String token
    );

    // =====================================================
// LISTAR PAGOS (SEGÚN ROL POR TOKEN)
// =====================================================
    @GET("api/pago/listarPagos.php")
    Call<PagoListaResponse> listarPagos(
            @Header("Authorization") String token,
            @Query("UsuarioId") int usuarioId
    );


    @FormUrlEncoded
    @POST("api/pago/crear.php")
    Call<GenericResponse> registrarPago(
            @Field("ReservaId") int reservaId,
            @Field("Monto") double monto,
            @Field("MedioDePago") String medio
    );

    @FormUrlEncoded
    @POST("api/pago/confirmarPago.php")
    Call<GenericResponse> confirmarPago(
            @Field("ReservaId") int reservaId
    );


    @FormUrlEncoded
    @POST("api/pago/rechazarPago.php")
    Call<Void> rechazarPago(
            @Field("PagoId") int pagoId,
            @Field("ReservaId") int reservaId
    );
// --------------------------------------------------
// NOTIFICACIONES
// --------------------------------------------------

    @GET("api/notificacion/listarnoti.php")
    Call<NotificacionListaResponse> listarNotificaciones(
            @Query("UsuarioId") int usuarioId
    );
    @FormUrlEncoded
    @POST("api/notificacion/marcarLeida.php")
    Call<GenericResponse> marcarNotificacionLeida(
            @Field("Id") int id,
            @Field("UsuarioId") int usuarioId
    );
    @FormUrlEncoded
    @POST("api/reserva/confirmarReserva.php")
    Call<GenericResponse> confirmarReserva(@Field("ReservaId") int reservaId);

    @FormUrlEncoded
    @POST("api/reserva/rechazarReserva.php")
    Call<GenericResponse> rechazarReserva(@Field("ReservaId") int reservaId);

    @FormUrlEncoded
    @POST("api/reserva/cancelarReserva.php")
    Call<GenericResponse> cancelarReserva(@Field("Id") int reservaId);


    @GET("api/reserva/listarPorUsuario.php")
    Call<ReservaListaResponse> listarReservas(
            @Query("Rol") String rol,
            @Query("ClienteId") Integer clienteId,
            @Query("UsuarioId") Integer usuarioId
    );
    @GET("api/usuarios/listarUsuarios.php")
    Call<UsuarioListaResponse> listarUsuarios(
            @Header("Authorization") String token
    );


    @FormUrlEncoded
    @POST("api/usuarios/eliminarUsuario.php")
    Call<GenericResponse> eliminarUsuario(
            @Header("Authorization") String token,
            @Field("Id") int id
    );


}


