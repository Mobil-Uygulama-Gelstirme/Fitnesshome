package com.example.fitnesshome;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class anaekranactivity extends AppCompatActivity {

    private Button btnkullanici,btnantrenman,btnhavadurumu,btnharita,bodyMassBtn;
    private FirebaseAuth mAuth;
    private Toolbar actionbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.oturumkapatma){
            mAuth.signOut();
            Toast.makeText(getApplicationContext(), "Oturum Kapatıldı", Toast.LENGTH_SHORT).show();
            Intent loginintent = new Intent(anaekranactivity.this, MainActivity.class);
            startActivity(loginintent);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anaekranactivity);
        mAuth= FirebaseAuth.getInstance();
        Toolbar actionbar = findViewById(R.id.actionBar);
        setSupportActionBar(actionbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Fitness Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnkullanici=findViewById(R.id.hesabimbtn);
        btnkullanici.setOnClickListener(view -> {
            Intent intentkullanici = new Intent(anaekranactivity.this,kullaniciactivity.class);
            startActivity(intentkullanici);
        });

        btnantrenman=findViewById(R.id.antrnmnbtn);
        btnantrenman.setOnClickListener(view -> {
            Intent intentAnt= new Intent(getBaseContext(),antrenmanProgramlari_activity.class);
            startActivity(intentAnt);
        });

        bodyMassBtn=findViewById(R.id.bodyMassBtn);
        bodyMassBtn.setOnClickListener(view ->{
            Intent intentAnt= new Intent(getBaseContext(), BodyMaskIndexActivity.class);
            startActivity(intentAnt);
        });

        btnharita=findViewById(R.id.haritabtn);
        btnharita.setOnClickListener(view -> {
            Intent intentHarita= new Intent(getBaseContext(),MapsActivity.class);
            startActivity(intentHarita);
        });

        btnhavadurumu=findViewById(R.id.havadrmbtn);
        btnhavadurumu.setOnClickListener(view -> {
            Intent intentHavaDurumu= new Intent(getBaseContext(),HavaDurumuActivity.class);
            startActivity(intentHavaDurumu);
        });

        if (mAuth.getCurrentUser() == null) {

            Toast.makeText(getApplicationContext(), "mAuth.getCurrentUser() == null", Toast.LENGTH_LONG).show();
            Intent girisIntent = new Intent(anaekranactivity.this, girisactivity.class);
            startActivity(girisIntent);
            Toast.makeText(getApplicationContext(), "Lütfen Giriş Yapınız", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}