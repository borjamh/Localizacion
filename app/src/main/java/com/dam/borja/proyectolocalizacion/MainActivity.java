package com.dam.borja.proyectolocalizacion;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    EditText textLong, textLat;
    ToggleButton actualizaciones;
    GoogleApiClient apiClient;
    LocationRequest locationRequest;
    LocationListener listenerGPS;
    Button btnMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicial_activity);
        instancias();
        acciones();

    }

    private void acciones() {

        actualizaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    locationRequest = new LocationRequest();
                    locationRequest.setInterval(2000);
                    locationRequest.setFastestInterval(1000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    /*LocationSettingsRequest locSettingsRequest =
                            new LocationSettingsRequest.Builder()
                                    .addLocationRequest(locationRequest)
                                    .build();

                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(
                                    apiClient, locSettingsRequest);


                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult locationSettingsResult) {
                            final Status status = locationSettingsResult.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    comenzarActualizaciones();
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            }
                        }
                    });*/

                    comenzarActualizaciones();
                }
                else desactivarActualizaciones();
            }
        });

        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapActivity.class);
                startActivity(i);
            }
        });

    }

    private void instancias() {
        btnMaps = (Button) findViewById(R.id.boton_mapas);
        textLat = (EditText) findViewById(R.id.edit_latitud);
        textLong = (EditText) findViewById(R.id.edit_longitud);
        actualizaciones = (ToggleButton) findViewById(R.id.toggle_actualizacion);
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void modificarDatos(Location l) {
        if (l != null) {
            textLat.setText(String.valueOf(l.getLatitude()));
            textLong.setText(String.valueOf(l.getLongitude()));
        } else {
            textLat.setText("Carga de latitud");
            textLong.setText("Carga de longitud");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("locaton", "Error en la gestion de los servicios de localizacion");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);
            modificarDatos(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        modificarDatos(location);
    }


    public void comenzarActualizaciones() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    apiClient, locationRequest, MainActivity.this);
        }
    }

    public void desactivarActualizaciones() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);
    }


}
