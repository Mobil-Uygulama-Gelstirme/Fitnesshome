package com.example.fitnesshome;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GetNearbyParksData extends AsyncTask<Object,String,String> {
    String googleParksData;
    GoogleMap googleMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        googleMap=(GoogleMap) objects[0];
        url= (String) objects[1];

        com.example.fitnesshome.DownloadUrl downloadUrl= new com.example.fitnesshome.DownloadUrl();
        try {
            googleParksData=downloadUrl.readUrl(url);//Servisten okuma işlemi
        } catch (IOException e){
            e.printStackTrace();
        }
        return googleParksData;
    }

    @Override
    protected void onPostExecute(String s) {//Task bitiminde UI thread'de yapılmasını istediğimiz şeyleri onPostExecute metodunu kullanıyoruz.
        try {
            JSONObject parentObject= new JSONObject(s);
            JSONArray resultArray=parentObject.getJSONArray("results");
            for(int i=0;i<resultArray.length();i++)
            {
                JSONObject jsonObject= resultArray.getJSONObject(i);
                JSONObject locationObj= jsonObject.getJSONObject("geometry").getJSONObject("location");

                String latitude = locationObj.getString("lat");//Lokasyon bilgileri
                String longitude = locationObj.getString("lng");
                JSONObject nameObject= resultArray.getJSONObject(i);
                String name = nameObject.getString("name");

                LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

                MarkerOptions markerOptions= new MarkerOptions();//Marker ekleme
                markerOptions.title(name);
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.park_marker));

                googleMap.addMarker(markerOptions);

            }

        } catch (JSONException e){
            e.printStackTrace();

        }
    }
}
