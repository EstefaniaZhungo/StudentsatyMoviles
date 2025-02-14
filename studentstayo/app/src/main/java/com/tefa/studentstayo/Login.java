package com.tefa.studentstayo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button btnLogin = findViewById(R.id.buttonLogin);
        TextView registrarse=findViewById(R.id.textViewLo);


        // Agregar evento de clic
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la pantalla principal (MainActivity)
                Intent intent = new Intent(Login.this, PantallaPrincipal.class);
                startActivity(intent);
                finish(); // Cierra la pantalla de Login para que no pueda volver atrás
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this, Registrar.class );
                startActivity(intent);
                finish();
            }
        });

    }
}
