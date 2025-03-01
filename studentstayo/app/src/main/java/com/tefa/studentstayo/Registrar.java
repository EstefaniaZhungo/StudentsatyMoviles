package com.tefa.studentstayo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.*;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.tefa.studentstayo.model.Persona;
import com.tefa.studentstayo.model.cliente;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Registrar extends AppCompatActivity {

    private TextInputEditText emailText, paswordText, auxcedula, auxnombre, auxnombre1, auxapelldio, auxapellido2, auxtelefono;
    private Button boton1;
    private TextView textView1, birthDateTextView;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_registrar);

        initViews();
        calendar();

        textView1.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        boton1.setOnClickListener(v -> {
            if (validarCampos()) {
                guardarPersona();
                PantallaPrincipal.correoUsuario = emailText.getText().toString();
                Toast.makeText(Registrar.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    private void initViews() {
        emailText = findViewById(R.id.email);
        paswordText = findViewById(R.id.contrasena);
        auxcedula = findViewById(R.id.cedula);
        auxnombre = findViewById(R.id.nombre1);
        auxnombre1 = findViewById(R.id.nombre2);
        auxapelldio = findViewById(R.id.apellido1);
        auxapellido2 = findViewById(R.id.apellido2);
        auxtelefono = findViewById(R.id.telefono);
        boton1 = findViewById(R.id.button);
        textView1 = findViewById(R.id.textViewRe);
        birthDateTextView = findViewById(R.id.birthDateTextView);
    }

    private boolean validarCampos() {
        if (esCampoVacio(emailText, "Ingrese su email")) return false;
        if (esCampoVacio(paswordText, "Ingrese su contraseña")) return false;
        if (esCampoVacio(auxcedula, "Ingrese su cédula") || auxcedula.getText().toString().length() != 10) {
            mostrarError(auxcedula, "La cédula debe tener 10 dígitos");
            return false;
        }
        if (!validarTexto(auxnombre, "Primer nombre inválido")) return false;
        if (!validarTexto(auxnombre1, "Segundo nombre inválido")) return false;
        if (!validarTexto(auxapelldio, "Apellido paterno inválido")) return false;
        if (!validarTexto(auxapellido2, "Apellido materno inválido")) return false;
        if (esCampoVacio(auxtelefono, "Ingrese su número de teléfono") || auxtelefono.getText().toString().length() != 10) {
            mostrarError(auxtelefono, "El teléfono debe tener 10 dígitos");
            return false;
        }
        return true;
    }

    private boolean esCampoVacio(TextInputEditText campo, String mensaje) {
        if (TextUtils.isEmpty(campo.getText().toString().trim())) {
            mostrarError(campo, mensaje);
            return true;
        }
        return false;
    }

    private boolean validarTexto(TextInputEditText campo, String mensaje) {
        if (esCampoVacio(campo, mensaje)) return false;
        if (!campo.getText().toString().matches("[a-zA-Z]+")) {
            mostrarError(campo, mensaje);
            return false;
        }
        return true;
    }

    private void mostrarError(TextInputEditText campo, String mensaje) {
        campo.setError(mensaje);
        campo.requestFocus();
    }

    private void calendar() {
        birthDateTextView.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setEnd(Calendar.getInstance().getTimeInMillis());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona tu fecha de nacimiento")
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            selectedDate = dateFormat.format(new Date(selection));
            birthDateTextView.setText(selectedDate);
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }

    private void guardarPersona() {
        Persona persona = new Persona();
        persona.setCedula_persona(auxcedula.getText().toString());
        persona.setNombre(auxnombre.getText().toString());
        persona.setNombre2(auxnombre1.getText().toString());
        persona.setApellido(auxapelldio.getText().toString());
        persona.setApellido2(auxapellido2.getText().toString());
        persona.setTelefono(auxtelefono.getText().toString());
        persona.setEdad(calcularEdad());

        realizarSolicitudPOST(Environment.BASE_URL + "/personas", persona);
        guardarClientes();
    }

    private void guardarClientes() {
        cliente clienteNuevo = new cliente();
        clienteNuevo.setContrasena(paswordText.getText().toString());
        clienteNuevo.setUsuario(emailText.getText().toString());
        clienteNuevo.setCedula_persona(auxcedula.getText().toString());

        realizarSolicitudPOST(Environment.BASE_URL + "/clientes", clienteNuevo);
    }

    private <T> void realizarSolicitudPOST(String url, final T objeto) {
        RequestQueue queue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        final String objetoJson = gson.toJson(objeto);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("TAG", "Error en la solicitud: " + error.toString())) {

            @Override
            public byte[] getBody() {
                return objetoJson.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private int calcularEdad() {
        if (selectedDate == null) return 0;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaNacimiento = LocalDate.parse(selectedDate, format);
        return fechaNacimiento.until(LocalDate.now()).getYears();
    }
}
