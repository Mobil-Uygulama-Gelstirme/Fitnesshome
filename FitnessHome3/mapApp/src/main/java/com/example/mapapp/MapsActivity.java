package com.example.mapapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

public class MapsActivity extends AppCompatActivity {


    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        LocationManager manager = (LocationManager) getSystemService(MapsActivity.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            
        }
        //Check permission
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When permission grated
            //Call Method
            getCurrentLocation();
        } else {
            //When permission denied
            //Request permission
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            getCurrentLocation();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Konumun kapalı görünüyor, aktif etmek ister misin?")
                .setCancelable(false)
                .setPositiveButton("Evet", (dialog, id) -> {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    this.recreate();
                })
                .setNegativeButton("Hayır", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();

    }
    public void getNearByParks(GoogleMap googleMap, @NotNull Location location) {

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + location.getLatitude() + "," + location.getLongitude() +
                "&radius=1000" +
                "&type=park" +
                "&key=" + getResources().getString(R.string.google_maps_key);

        Object[] dataTransfer = new Object[2];
        dataTransfer[0]=googleMap;
        dataTransfer[1]=url;

        GetNearbyParksData getNearbyParksData = new GetNearbyParksData();
        getNearbyParksData.execute(dataTransfer);
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //Initialize task Location

        client.getLastLocation().addOnCompleteListener(task -> {
            Location location= task.getResult();
            if(location!=null)
            {
                //Syc Map
                mapFragment.getMapAsync(googleMap -> {
                    //Add My Location Button
                    googleMap.setMyLocationEnabled(true);




                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    //Initialize Lat Lng
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    //Create Marker Options

                    //zoom Map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                    //Add Marker On Location

                    getNearByParks(googleMap,location);

                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==44){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //When permission grated
                //Call method
                getCurrentLocation();
            }
        }
    }
}