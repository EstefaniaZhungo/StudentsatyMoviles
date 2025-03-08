package com.tefa.studentstayo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Mapa extends AppCompatActivity {

    private MapView mapView;
    private static final String URL_HABITACIONES = Environment.BASE_URL + "/habitaciones";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa); // Asegúrate de tener este XML

        // Configuración de osmdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));

        // Inicializar el mapa
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(17);
        mapView.setMultiTouchControls(true);

        // Solicitar permisos de ubicación
        verificarPermisos();

        GeoPoint cuenca = new GeoPoint(-2.9006, -79.0048); // Coordenadas de Cuenca
        mapView.getController().setCenter(cuenca);

        // Cargar las ubicaciones de las habitaciones
        cargarUbicacionesHabitaciones();
    }

    private void verificarPermisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void cargarUbicacionesHabitaciones() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(URL_HABITACIONES).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Mapa", "Error al obtener habitaciones: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    runOnUiThread(() -> procesarUbicaciones(jsonResponse));
                }
            }
        });
    }

    private void procesarUbicaciones(String json) {
        try {
            JSONArray habitacionesArray = new JSONArray(json);
            mapView.getOverlays().clear(); // Limpiar marcadores previos

            for (int i = 0; i < habitacionesArray.length(); i++) {
                JSONObject habitacion = habitacionesArray.getJSONObject(i);
                double latitud = habitacion.getDouble("latitud");
                double longitud = habitacion.getDouble("longitud");
                int numeroHabitacion = habitacion.getInt("nHabitacion");
                String estado= habitacion.getString("estado");
                String des= habitacion.getString("descriphabi");

                agregarMarcador(latitud, longitud, numeroHabitacion, des, estado);
            }

            mapView.invalidate(); // Refrescar el mapa
        } catch (JSONException e) {
            Log.e("Mapa", "Error procesando JSON: " + e.getMessage());
        }
    }

    private void agregarMarcador(double lat, double lon, int numeroHabitacion, String des, String estado) {
        GeoPoint punto = new GeoPoint(lat, lon);
        Marker marcador = new Marker(mapView);
        marcador.setPosition(punto);
        marcador.setTitle("Habitación " + estado);
        marcador.setSubDescription("Descripción: $" + des);
        mapView.getOverlays().add(marcador);
    }
}

