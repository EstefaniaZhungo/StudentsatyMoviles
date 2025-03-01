package com.tefa.studentstayo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class Mapa extends AppCompatActivity {

    private MapView mapView;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa); // Debes crear este XML

        // Configuración de osmdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));

        // Inicializar el mapa
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(15);
        mapView.setMultiTouchControls(true);

        // Obtener la ubicación actual
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        obtenerUbicacion();
    }

    private void obtenerUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                actualizarUbicacion(location);
            }
        });
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            GeoPoint punto = new GeoPoint(location.getLatitude(), location.getLongitude());
            mapView.getController().setCenter(punto);

            // Agregar marcador
            Marker marcador = new Marker(mapView);
            marcador.setPosition(punto);
            marcador.setTitle("Ubicación actual");
            mapView.getOverlays().clear();
            mapView.getOverlays().add(marcador);
            mapView.invalidate();
        }
    }
}

