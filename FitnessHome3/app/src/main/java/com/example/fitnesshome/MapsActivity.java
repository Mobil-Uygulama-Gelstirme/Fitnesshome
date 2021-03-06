package com.example.fitnesshome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

public class MapsActivity extends AppCompatActivity {



    SupportMapFragment mapFragment;//Haritamızın görüntüleneceği fragment belirleniyor.
    CardView cardView;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        cardView=findViewById(R.id.cardView);

        LocationManager manager = (LocationManager) getSystemService(MapsActivity.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//Konum açık mı değil mi kontrol ediyoruz.
            buildAlertMessageNoGps();
        }
        //Check permission
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //İzin sağlanırsa
            //Metodu çağırıyoruz.
            getCurrentLocation();
        } else {
            //İzin red edilmişse izin istiyoruz.
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
                    LocationManager manager = (LocationManager) getSystemService(MapsActivity.LOCATION_SERVICE);
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));//Konum penceresi açılıyor.
                    while(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))//Konum açılasıya kadar bekler. Sonra yeniden başlatır.
                    {}
                    this.recreate();
                })
                .setNegativeButton("Hayır", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();

    }
    
    public void getNearByParks(GoogleMap googleMap, @NotNull Location location) {

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + location.getLatitude() + "," + location.getLongitude() +//Lokasyon bilgilerini giriyoruz.
                "&radius=1000" +//Gösterilecek mesafeyi belirliyoruz.
                "&type=park" +//park tipindeki yerlerin konum bilgilerini alıyoruz.
                "&key=" + getResources().getString(R.string.google_maps_key);//Servisimize billing hesaptan bir api key ile bağlanıyoruz.

        Object[] dataTransfer = new Object[2];
        dataTransfer[0]=googleMap;
        dataTransfer[1]=url;

        com.example.fitnesshome.GetNearbyParksData getNearbyParksData = new com.example.fitnesshome.GetNearbyParksData();
        getNearbyParksData.execute(dataTransfer);
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        cardView.setVisibility(View.VISIBLE);//Progress Bar gösteriliyor.

        LocationRequest locationRequest= new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(MapsActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(MapsActivity.this).removeLocationUpdates(this);
                Location location = locationResult.getLastLocation();
                mapFragment.getMapAsync(googleMap -> {
                    //Lokasyon gösteriliyor.
                    googleMap.setMyLocationEnabled(true);
                    //Lokasyonum butonu ekleniyor.
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    //Lat Lng belirleniyor.
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                    //zoom Map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    //Çevredeki parklar gösteriliyor.
                    getNearByParks(googleMap,location);
                    cardView.setVisibility(View.GONE);//Progress bar sonlanıyor.
                });
            }}, Looper.getMainLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //İzin sağlandığında metod çağrılıyor
                getCurrentLocation();
            }
            else
            {
                Toast.makeText(this,"Konum izni reddedildi!",Toast.LENGTH_LONG).show();
            }
        }
    }
}