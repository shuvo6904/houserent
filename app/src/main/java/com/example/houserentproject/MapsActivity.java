package com.example.houserentproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    GoogleMap myMap;
    double latOfHostel, lonOfHostel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Hide the title
        getSupportActionBar().hide(); // Hide title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_maps);

        latOfHostel = getIntent().getDoubleExtra("lat", 22.793528665164757);
        lonOfHostel = getIntent().getDoubleExtra("lon", 91.10448741362408);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapDetailsId);
        mapFragment.getMapAsync(MapsActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        myMap = googleMap;

        LatLng hostelLocation = new LatLng(latOfHostel, lonOfHostel);
        myMap.addMarker(new MarkerOptions().position(hostelLocation).title("Hostel is Here"));
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hostelLocation, 17));


    }
}