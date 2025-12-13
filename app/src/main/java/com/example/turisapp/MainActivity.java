package com.example.turisapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turisapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show()
        );

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // ============================================
        // Cargar datos del header (nombre, rol, avatar)
        // ============================================
        View headerView = navigationView.getHeaderView(0);

        TextView tvBienvenida = headerView.findViewById(R.id.textViewBienvenida);
        TextView tvRol = headerView.findViewById(R.id.textView);
        ImageView ivAvatar = headerView.findViewById(R.id.imageViewAvatar);

        // Leer SharedPreferences
        SharedPreferences sp = getSharedPreferences("usuario.xml", MODE_PRIVATE);
        String nombre = sp.getString("nombre", "Usuario");
        String rol = sp.getString("rol", "Rol");
        String avatar = sp.getString("avatar", "");

        // Setear textos
        tvBienvenida.setText("¡Hola " + nombre + "!");
        tvRol.setText("Rol: " + rol);


        String urlAvatar = "http://10.0.2.2:5000/" + avatar;

        Glide.with(this)
                .load(urlAvatar)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivAvatar);

        // ============================================

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.alojamientosFragment,
                R.id.perfilFragment,
                R.id.registroFragment,
                R.id.nav_home,
                R.id.nav_reservas,     // 🔥 AGREGADO
                R.id.nav_pagos
        )
                .setOpenableLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(item -> {
            System.out.println("CLICK MENU ===> " + item.getItemId());

            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

            if (!handled) {
                System.out.println("NavigationUI NO reconoció el destino");
            }

            DrawerLayout draweri = binding.drawerLayout;
            draweri.closeDrawers();
            return handled;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
