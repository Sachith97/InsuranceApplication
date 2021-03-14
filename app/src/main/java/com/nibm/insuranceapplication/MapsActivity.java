package com.nibm.insuranceapplication;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng colombo = new LatLng(6.9063951,79.8684273);
        mMap.addMarker(new MarkerOptions().position(colombo).title("Colombo"));
        float zoomLevel = 10.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(colombo, zoomLevel));

        LatLng gampaha = new LatLng(7.0815065,79.9772934);
        mMap.addMarker(new MarkerOptions().position(gampaha).title("Gampaha"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gampaha));

        LatLng nugegoda = new LatLng(6.8726545,79.8895848);
        mMap.addMarker(new MarkerOptions().position(nugegoda).title("Nugegoda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nugegoda));

        LatLng kiribathgoda = new LatLng(6.9780513,79.9226811);
        mMap.addMarker(new MarkerOptions().position(kiribathgoda).title("kiribathgoda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kiribathgoda));


    }
}