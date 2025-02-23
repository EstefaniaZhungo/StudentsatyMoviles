package com.tefa.studentstayo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PantallaPerfilUsuario extends AppCompatActivity {
    private String cedula;
    private Long idCliente;
    private EditText txtTelefono, txtnombre, txtnombre2, txtapellido, txtapellido2, txtcontrasena;
    private Button btnEditar;
    private int edad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfil_usuario);
        // Cargar datos iniciales (incluye foto de perfil)
        getDatos(PantallaPrincipal.correoUsuario);
        // Configurar el botón de actualización para modificar los datos del usuario/persona (excepto la foto)
        actualizarDatos();
    }

    public void actualizarDatos() {
        btnEditar = findViewById(R.id.btnEditar);
        txtnombre = findViewById(R.id.txtNombre);
        txtnombre2 = findViewById(R.id.txtNombre2);
        txtapellido = findViewById(R.id.txtApellido);
        txtapellido2 = findViewById(R.id.txtApellido2);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtcontrasena = findViewById(R.id.pasword);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualiza solo la contraseña y el usuario en la tabla clientes
                updateDatosUsuario(txtcontrasena.getText().toString(), PantallaPrincipal.correoUsuario, idCliente);
                // Actualiza solo los datos personales (nombre, apellido, teléfono) en la tabla personas
                updateDatosPersona(
                        txtnombre.getText().toString(),
                        txtnombre2.getText().toString(),
                        txtapellido.getText().toString(),
                        txtapellido2.getText().toString(),
                        txtTelefono.getText().toString(),
                        edad,
                        cedula
                );
                salir(v);
            }
        });
    }

    // Metodo para obtener los datos del cliente, incluyendo la foto de perfil
    public void getDatos(String usuario) {
        String url = Environment.BASE_URL + "/clientes/usuario/" + usuario;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                JSONObject jsonObjectCliente = response.getJSONObject(0);
                                cedula = jsonObjectCliente.getString("cedula_persona");
                                idCliente = jsonObjectCliente.getLong("idCliente");
                                String contrasena = jsonObjectCliente.getString("contrasena");
                                String imagenBase64 = jsonObjectCliente.getString("foto");
                                if (imagenBase64.startsWith("data:image")) {
                                    imagenBase64 = imagenBase64.split(",")[1];
                                }
                                Bitmap imagenBitmap = decodeBase64ToBitmap(imagenBase64);
                                ImageView imgPerfil = findViewById(R.id.imgPerfil);
                                imgPerfil.setImageBitmap(imagenBitmap);

                                txtcontrasena = findViewById(R.id.pasword);
                                txtcontrasena.setText(contrasena);

                                getDatosByCedula(cedula);
                            } else {
                                Toast.makeText(getApplicationContext(), "Cliente no encontrado", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException j) {
                            j.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error en la solicitud", error);
            }
        }
        );
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    // Metodo para decodificar una cadena Base64 a Bitmap
    public Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (IllegalArgumentException e) {
            Log.e("Base64 Error", "Error al decodificar Base64", e);
            return null;
        }
    }

    // Metodo para obtener los datos de la persona
    public void getDatosByCedula(String cedula) {
        String url = Environment.BASE_URL + "/personas/" + cedula;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.length() > 0) {
                                String nombre = response.getString("nombre");
                                String nombre2 = response.getString("nombre2");
                                String apellido = response.getString("apellido");
                                String apellido2 = response.getString("apellido2");
                                String telefono = response.getString("telefono");
                                edad = response.getInt("edad");

                                txtnombre = findViewById(R.id.txtNombre);
                                txtnombre2 = findViewById(R.id.txtNombre2);
                                txtapellido = findViewById(R.id.txtApellido);
                                txtapellido2 = findViewById(R.id.txtApellido2);
                                txtTelefono = findViewById(R.id.txtTelefono);

                                txtnombre.setText(nombre);
                                txtnombre2.setText(nombre2);
                                txtapellido.setText(apellido);
                                txtapellido2.setText(apellido2);
                                txtTelefono.setText(telefono);
                            } else {
                                Toast.makeText(getApplicationContext(), "Persona no encontrada", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException j) {
                            j.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSON ERROR", error.getMessage());
            }
        }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    // Metodo para actualizar los datos del cliente (usuario y contraseña)
    public void updateDatosUsuario(String contrasena, String usuario, Long id) {
        String url = Environment.BASE_URL + "/clientes/" + id;
        JSONObject requestBodyUsuario = new JSONObject();
        try {
            requestBodyUsuario.put("contrasena", contrasena);
            requestBodyUsuario.put("usuario", usuario);
        } catch (JSONException j) {
            j.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequestUsuario = new JsonObjectRequest(Request.Method.PUT, url, requestBodyUsuario,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "ACTUALIZACIÓN CORRECTA", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequestUsuario);
    }

    // Metodo para actualizar los datos de la persona (nombre, apellido y teléfono)
    public void updateDatosPersona(String nom1, String nom2, String ape1, String ape2, String tel, int edad, String cedula) {
        String url = Environment.BASE_URL + "/personas/" + cedula;
        JSONObject requestBodyPersona = new JSONObject();
        try {
            requestBodyPersona.put("nombre", nom1);
            requestBodyPersona.put("nombre2", nom2);
            requestBodyPersona.put("apellido", ape1);
            requestBodyPersona.put("apellido2", ape2);
            requestBodyPersona.put("telefono", tel);
        } catch (JSONException j) {
            j.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequestPersona = new JsonObjectRequest(Request.Method.PUT, url, requestBodyPersona,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "ACTUALIZACIÓN CORRECTA", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequestPersona);
    }

    // Método para salir de la actividad
    public void salir(View view) {
        Intent inicio = new Intent(this, PantallaPrincipal.class);
        startActivity(inicio);
        finish();
    }
}
