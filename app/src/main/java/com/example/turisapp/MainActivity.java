package com.example.turisapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.turisapp.databinding.ActivityMainBinding;
import com.example.turisapp.modelo.MenuRoleConfigurator;
import com.example.turisapp.request.ApiClient;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.alojamientosFragment,
                R.id.nav_crear_alojamiento,
                R.id.editarAlojamientoFragment,
                R.id.nav_home,
                R.id.perfilFragment,
                R.id.nav_pagos

        )
                .setOpenableLayout(drawer)
                .build();

        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            SharedPreferences sp = getSharedPreferences("datos", MODE_PRIVATE);
            String rol = sp.getString("rol", null); // null = INVITADO

            MenuRoleConfigurator.aplicar(navigationView.getMenu(), rol);
        });


        cargarHeaderUsuario(navigationView);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        String rol = ApiClient.leerRol(this); // 👈 usar Activity, no Fragment
        MenuRoleConfigurator.aplicar(menu, rol);

        return true;
    }


    private void cargarHeaderUsuario(NavigationView navigationView) {

        View headerView = navigationView.getHeaderView(0);

        TextView tvBienvenida = headerView.findViewById(R.id.textViewBienvenida);
        TextView tvRol = headerView.findViewById(R.id.textView);
        ImageView ivAvatar = headerView.findViewById(R.id.imageViewAvatar);

        SharedPreferences sp = getSharedPreferences("datos", MODE_PRIVATE);

        String nombre = sp.getString("nombre", "Invitado");
        String rol = sp.getString("rol", "INVITADO");
        String avatar = sp.getString("avatar", "");

        tvBienvenida.setText("¡Hola " + nombre + "!");
        tvRol.setText("Rol: " + rol);

        String urlAvatar = avatar.isEmpty()
                ? null
                : "http://192.168.100.7/turisapp/api/uploads/avatars/" + avatar;

        Glide.with(this)
                .load(urlAvatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .circleCrop()
                .into(ivAvatar);
    }


    @Override
    protected void onResume() {
        super.onResume();

        NavigationView navView = binding.navView;

        SharedPreferences sp = getSharedPreferences("datos", MODE_PRIVATE);
        String rol = sp.getString("rol", null); // null = INVITADO

        MenuRoleConfigurator.aplicar(navView.getMenu(), rol);
        cargarHeaderUsuario(navView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
