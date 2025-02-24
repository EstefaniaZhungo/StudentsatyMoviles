package com.tefa.studentstayo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tefa.studentstayo.model.Habi;
import com.tefa.studentstayo.model.MyAdapter;
import com.tefa.studentstayo.model.Reservas;
import com.tefa.studentstayo.model.Servi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Servicio extends AppCompatActivity {
    RecyclerView recyclerView;
    MyAdapter adapter;
    ArrayList<Servi> servis = new ArrayList<>();
    Spinner spinner;
    List<Habi> habitacion = PantallaPrincipal.habitacion; // Lista de habitaciones
    List<Reservas> reservas = new ArrayList<>(); // Lista de reservas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio);

        // Animación del botón
        TextView button1 = findViewById(R.id.textView9);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button1, "scaleX", 1.3f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button1, "scaleY", 1.3f);
        scaleX.setDuration(2000);
        scaleY.setDuration(2000);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
        scaleX.setInterpolator(new BounceInterpolator());
        scaleY.setInterpolator(new BounceInterpolator());
        scaleX.start();
        scaleY.start();

        getDatos(); // Obtener los servicios
        getReservas(); // Obtener las reservas

        recyclerView = findViewById(R.id.recyclerViewhabi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, servis);
        recyclerView.setAdapter(adapter);

        // Insertar una opción para "Seleccionar" al principio de la lista de habitaciones
        Habi opcionSeleccione = new Habi();
        opcionSeleccione.setIdHabitaciones(0L);
        habitacion.add(0, opcionSeleccione);

        // Llenar el spinner con las habitaciones
        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<Habi> adapterh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, habitacion);
        adapterh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterh);

        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                // Obtener la reserva seleccionada en el Spinner
                Reservas reservaSeleccionada = (Reservas) spinner.getSelectedItem();

                if (reservaSeleccionada != null && reservaSeleccionada.getIdReserva() != 0) {
                    // Obtener el idHabitaciones desde la reserva seleccionada
                    Long idHabitaciones = reservaSeleccionada.getIdHabitaciones();
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Servicio.this);
                    builder.setTitle("Describa su Servicio");
                    final EditText input = new EditText(Servicio.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setTextColor(Color.DKGRAY);
                    builder.setView(input);
                    builder.setPositiveButton("Solicitar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String descripcion = input.getText().toString();
                            Servi serviSeleccionado = servis.get(position);
                            long idTipo_servicio = serviSeleccionado.getIdTipo_servicio();
                            String estado = "Pendiente";
                            guardarDatos(descripcion, idHabitaciones, idTipo_servicio, estado);
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Servicio.this, R.style.MyAlertDialogTheme);
                            builder.setTitle("¡Servicio Solicitado!");
                            builder.setMessage("Tu solicitud de servicio ha sido procesada correctamente. Estaremos en contacto contigo pronto.");
                            builder.setIcon(android.R.drawable.ic_dialog_info);
                            builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                v.vibrate(1000);
                            }
                            builder.show();
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    new AlertDialog.Builder(Servicio.this)
                            .setTitle("Alerta")
                            .setMessage("Selecciona una reserva primero")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
    }

    // Método para obtener los servicios (getDatos) y las reservas (getReservas)
    private void getDatos() {
        String url = Environment.BASE_URL + "/tiposervicio"; // Endpoint de servicios
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pasarJson(response); // Pasar los datos de servicios
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", "Error al obtener los servicios: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error al obtener los servicios", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private void pasarJson(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                Servi servi = new Servi();
                servi.setIdTipo_servicio(json.getInt("idTipo_servicio"));
                servi.setTitulo(json.getString("titulo"));
                servi.setDescripcion(json.getString("descripciontipo"));
                String base64Image = json.getString("foto");
                if (base64Image.startsWith("data:image/jpeg;base64,")) {
                    base64Image = base64Image.substring("data:image/jpeg;base64,".length());
                } else if (base64Image.startsWith("data:image/jpg;base64,")) {
                    base64Image = base64Image.substring("data:image/jpg;base64,".length());
                } else if (base64Image.startsWith("data:image/png;base64,")) {
                    base64Image = base64Image.substring("data:image/png;base64,".length());
                }
                servi.setFoto(base64Image);
                servis.add(servi);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("JSON Error", "Error procesando el JSON: " + e.getMessage());
        }
    }

    // Método para obtener las reservas
    private void getReservas() {
        String url = Environment.BASE_URL + "/reservas"; // Endpoint de reservas

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pasarReservas(response); // Pasar las reservas al Spinner
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", "Error al obtener las reservas: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error al obtener las reservas", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private void pasarReservas(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                Reservas reserva = new Reservas();
                reserva.setIdReserva(json.getLong("idReserva"));
                reserva.setIdHabitaciones(json.getLong("idHabitaciones")); // Asignar idHabitaciones desde la reserva
                reservas.add(reserva);
            }

            // Asegurándonos de que la lista de reservas no esté vacía
            if (!reservas.isEmpty()) {
                Reservas opcionSeleccione = new Reservas();
                opcionSeleccione.setIdReserva(0L); // ID de reserva 0 representa "ninguna reserva"
                reservas.add(0, opcionSeleccione);
            }

            // Ahora llenamos el Spinner con las reservas
            ArrayAdapter<Reservas> adapterh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, reservas);
            adapterh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterh);

        } catch (JSONException e) {
            Log.e("JSON Error", "Error procesando las reservas JSON: " + e.getMessage());
        }
    }

    private void guardarDatos(String descripcion, Long idHabitaciones, Long idTipo_servicio, String estado) {
        String url = Environment.BASE_URL + "/servicio";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("descripcion", descripcion);
            jsonBody.put("idHabitaciones", idHabitaciones); // Usar idHabitaciones de la reserva
            jsonBody.put("idTipo_servicio", idTipo_servicio);
            jsonBody.put("estado", estado);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Aquí puedes manejar la respuesta
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("API Error", "Error al guardar el servicio: " + error.getMessage());
                }
            });

            Volley.newRequestQueue(this).add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("Error", "Error en guardarDatos: " + e.getMessage());
        }
    }
}






