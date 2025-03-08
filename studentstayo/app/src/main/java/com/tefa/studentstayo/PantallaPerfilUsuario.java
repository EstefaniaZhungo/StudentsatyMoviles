package com.tefa.studentstayo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.tefa.studentstayo.model.Persona;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class PantallaPerfilUsuario extends AppCompatActivity {

    private String cedula, imagenBase64;
    private Long idCliente;
    private int edad;
    private EditText txtTelefono, txtNombre, txtNombre2, txtApellido, txtApellido2, txtContrasena;
    private Button btnEditar;
    private ImageView imgPerfil;
    private RequestQueue requestQueue;
    private Persona persona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfil_usuario);

        requestQueue = Volley.newRequestQueue(this);
        inicializarUI();
        cargarDatos(PantallaPrincipal.correoUsuario);
    }

    private void inicializarUI() {
        txtNombre = findViewById(R.id.txtNombre);
        txtNombre2 = findViewById(R.id.txtNombre2);
        txtApellido = findViewById(R.id.txtApellido);
        txtApellido2 = findViewById(R.id.txtApellido2);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtContrasena = findViewById(R.id.pasword);
        imgPerfil = findViewById(R.id.imgPerfil);
        btnEditar = findViewById(R.id.btnEditar);

        btnEditar.setOnClickListener(v -> actualizarDatos());
    }

    private void cargarDatos(String usuario) {
        String url = Environment.BASE_URL + "/clientes/usuario/" + usuario;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.length() > 0) {
                    JSONObject jsonObjectCliente = response.getJSONObject(0);
                    cedula = jsonObjectCliente.optString("cedula_persona");
                    idCliente = jsonObjectCliente.optLong("idCliente");
                    txtContrasena.setText(jsonObjectCliente.optString("contrasena"));

                    imagenBase64 = jsonObjectCliente.optString("foto", "");
                    if (!imagenBase64.isEmpty()) {
                        imgPerfil.setImageBitmap(decodeBase64ToBitmap(imagenBase64));
                    }

                    if (!cedula.isEmpty()) {
                        cargarDatosPersona(cedula);
                    }
                } else {
                    mostrarMensaje("Cliente no encontrado");
                }
            } catch (JSONException e) {
                manejarError(e);
            }
        }, this::manejarVolleyError);

        requestQueue.add(request);
    }

    private void cargarDatosPersona(String cedula) {
        String url = Environment.BASE_URL + "/personas/" + cedula;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            persona = new Persona(
                    cedula,
                    response.optString("nombre", ""),
                    response.optString("nombre2", ""),
                    response.optString("apellido", ""),
                    response.optString("apellido2", ""),
                    response.optString("telefono", ""),
                    response.optString("direccion", ""),
                    response.optInt("edad", 0),
                    response.optString("genero", ""),
                    response.optString("id_canton", ""),
                    response.optString("nombreContactoEmergencia", ""),
                    response.optString("telefonoContactoEmergencia", ""),
                    response.optString("parentescoContactoEmergencia", ""),
                    response.optString("nombreContactoEmergencia2", ""),
                    response.optString("telefonoContactoEmergencia2", ""),
                    response.optString("parentescoContactoEmergencia2", "")

            );

            // Cargar los datos en los campos de texto
            txtNombre.setText(persona.getNombre());
            txtNombre2.setText(persona.getNombre2());
            txtApellido.setText(persona.getApellido());
            txtApellido2.setText(persona.getApellido2());
            txtTelefono.setText(persona.getTelefono());

        }, this::manejarVolleyError);

        requestQueue.add(request);
    }


    private void actualizarDatos() {
        if (idCliente != null && cedula != null) {
            actualizarUsuario();
            actualizarPersona();
            // Refrescar la actividad después de actualizar los datos
            new android.os.Handler().postDelayed(this::recreate, 1000);
        } else {
            mostrarMensaje("No se pudieron actualizar los datos");
        }
    }

    private void actualizarUsuario() {
        String url = Environment.BASE_URL + "/clientes/" + idCliente;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("contrasena", txtContrasena.getText().toString());
            requestBody.put("usuario", PantallaPrincipal.correoUsuario);
            requestBody.put("foto", imagenBase64); // Enviar la misma imagen ya cargada
        } catch (JSONException e) {
            manejarError(e);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                response -> mostrarSnackbar("Datos actualizado correctamente"), this::manejarVolleyError);

        requestQueue.add(request);
    }

    private void mostrarSnackbar(String mensaje) {
        Snackbar.make(findViewById(android.R.id.content), mensaje, Snackbar.LENGTH_LONG).show();
    }

    private void actualizarPersona() {
        if (persona == null) return;
        String url = Environment.BASE_URL + "/personas/" + cedula;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("nombre", txtNombre.getText().toString().isEmpty() ? persona.getNombre() : txtNombre.getText().toString());
            requestBody.put("nombre2", txtNombre2.getText().toString().isEmpty() ? persona.getNombre2() : txtNombre2.getText().toString());
            requestBody.put("apellido", txtApellido.getText().toString().isEmpty() ? persona.getApellido() : txtApellido.getText().toString());
            requestBody.put("apellido2", txtApellido2.getText().toString().isEmpty() ? persona.getApellido2() : txtApellido2.getText().toString());
            requestBody.put("telefono", txtTelefono.getText().toString().isEmpty() ? persona.getTelefono() : txtTelefono.getText().toString());
            requestBody.put("direccion", persona.getDireccion());
            requestBody.put("edad", persona.getEdad());
            requestBody.put("genero", persona.getGenero()); // Si no se edita, se mantiene
            requestBody.put("id_canton", persona.getId_canton());
            requestBody.put("nombreContactoEmergencia", persona.getNombreContactoEmergencia());
            requestBody.put("telefonoContactoEmergencia", persona.getTelefonoContactoEmergencia());
            requestBody.put("parentescoContactoEmergencia", persona.getParentescoContactoEmergencia());
            requestBody.put("nombreContactoEmergencia2", persona.getNombreContactoEmergencia2());
            requestBody.put("telefonoContactoEmergencia2", persona.getTelefonoContactoEmergencia2());
            requestBody.put("parentescoContactoEmergencia2", persona.getParentescoContactoEmergencia2());
        } catch (JSONException e) {
            manejarError(e);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                response -> {
                }, this::manejarVolleyError);

        requestQueue.add(request);
    }


    private Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            if (base64String.startsWith("data:image")) {
                base64String = base64String.split(",")[1];
            }
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.e("Base64 Error", "Error al decodificar Base64", e);
            return null;
        }
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void manejarError(Exception e) {
        Log.e("Error", "Error en la aplicación", e);
    }

    private void manejarVolleyError(VolleyError error) {
        Log.e("VolleyError", "Error en la solicitud", error);
        mostrarMensaje("Error en la solicitud: " + error.getMessage());
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
    }

    private void salir() {
        Intent intent = new Intent(this, PantallaPrincipal.class);
        startActivity(intent);
        finish();
    }
}
