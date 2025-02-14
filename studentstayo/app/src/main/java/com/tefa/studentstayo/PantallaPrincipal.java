package com.tefa.studentstayo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PantallaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        // Referencias a los botones
        Button buttonPerfil = findViewById(R.id.buttonPerfil);
        Button buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion);
        Button buttonServicios = findViewById(R.id.buttonServicios);

        // Evento para ir a la pantalla de perfil
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipal.this, PantallaPerfilUsuario.class);
                startActivity(intent);
            }
        });

        // Evento para cerrar sesión y volver a la pantalla de Login
        buttonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipal.this, Login.class);
                startActivity(intent);
                finish(); // Cierra la pantalla principal para evitar regresar con el botón atrás
            }
        });

        // Evento para ir a la pantalla de servicios
        buttonServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantallaPrincipal.this, Servicio.class);
                startActivity(intent);
            }
        });
    }
}

