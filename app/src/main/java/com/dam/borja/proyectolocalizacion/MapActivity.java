package com.dam.borja.proyectolocalizacion;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.dam.borja.proyectolocalizacion.adaptadores.AdaptadorSpinner;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, CompoundButton.OnCheckedChangeListener

{

    GoogleMap mapa;
    MapFragment mapFragment;
    Spinner spinner_map_nav, spinner_zoom_nav;
    GoogleApiClient apiClient;
    Location locazacionActual;
    int zoom = 1;
    NavigationView navigationView;
    LocationRequest locationRequest;
    LocationListener locationListener;
    Switch aSwitch, switchPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymap_layout);
        instancias();
        persoSpinner();
        acciones();
        mapFragment.getMapAsync(this);
    }

    private void acciones() {
        spinner_map_nav.setOnItemSelectedListener(this);
        spinner_zoom_nav.setOnItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuitem_localizacion:
                        updateActualLocation();
                        break;
                    case R.id.marker_position:
                        crearMarcador();
                        break;
                }


                return true;
            }
        });
        aSwitch.setOnCheckedChangeListener(this);
        switchPosition.setOnCheckedChangeListener(this);

    }

    public void crearMarcador() {
        mapa.addMarker(new MarkerOptions()
                .title("Marcador propio")
                .snippet("Ejemplo de snippet")
                .position(new LatLng(locazacionActual.getLatitude(), locazacionActual.getLongitude())));
    }

    private void persoSpinner() {
        ArrayList lista = new ArrayList();
        lista.add("Normal");
        lista.add("Satellite");
        lista.add("Hibrida");

        ArrayList lista2 = new ArrayList();
        lista2.add("1X");
        lista2.add("2X");
        lista2.add("5X");
        lista2.add("10X");
        lista2.add("20X");
        lista2.add("30X");

        //spinner_map_nav.setAdapter(new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_list_item_1, lista));

        spinner_map_nav.setAdapter(new AdaptadorSpinner(MapActivity.this));
        spinner_zoom_nav.setAdapter(new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_list_item_1, lista2));

    }

    private void instancias() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapa);
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        navigationView = (NavigationView) findViewById(R.id.navigation);
        spinner_map_nav = (Spinner) navigationView.getMenu().findItem(R.id.spinner_vista_nav).getActionView();
        spinner_zoom_nav = (Spinner) navigationView.getMenu().findItem(R.id.spinner_zoom_map).getActionView();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        aSwitch = (Switch) navigationView.getMenu().findItem(R.id.switch_loc_map).getActionView();
        switchPosition = (Switch) navigationView.getMenu().findItem(R.id.switch_pos_map).getActionView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
    }

    public void updateActualLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {

            locazacionActual =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(locazacionActual.getLatitude(), locazacionActual.getLongitude()), zoom);
            mapa.moveCamera(cameraUpdate);
        }
    }

    public int changeZoom(int i) {
        zoom = i;
        return zoom;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_vista_nav:

                switch (position) {
                    case 0:
                        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                }
                mapa.getUiSettings().setZoomControlsEnabled(true);

                break;

            case R.id.spinner_zoom_map:

                switch (position) {
                    case 0:
                        zoom = 0;
                        break;
                    case 1:
                        zoom = 3;
                        break;
                    case 2:
                        zoom = 6;
                        break;
                    case 3:
                        zoom = 10;
                        break;
                    case 4:
                        zoom = 15;
                        break;
                    case 5:
                        zoom = 20;
                        break;
                }

                changeZoom(zoom);

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {

            locazacionActual =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_loc_map:
                if (isChecked) {
                    if (ActivityCompat.checkSelfPermission(MapActivity.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                apiClient, locationRequest, MapActivity.this);
                    }
                }
            case R.id.switch_pos_map:
                if (isChecked) {
                    mapa.setMyLocationEnabled(true);
                } else mapa.setMyLocationEnabled(false);
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom);


        CameraUpdate cameraUpdate1 = CameraUpdateFactory.zoomBy(5);
        mapa.moveCamera(cameraUpdate1);


    }
}
