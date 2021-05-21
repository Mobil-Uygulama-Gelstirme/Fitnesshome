package com.example.fitnesshome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnWelcomeLogin, btnWelcomeRegister;

    public void init(){
        btnWelcomeLogin = findViewById(R.id.btnWelcomeLogin);
        btnWelcomeRegister = findViewById(R.id.btnWelcomeRegister);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btnWelcomeLogin.setOnClickListener(view -> {
            Intent intentLogin = new Intent(MainActivity.this, girisactivity.class);
            startActivity(intentLogin);
            finish();
        });

        btnWelcomeRegister.setOnClickListener(view -> {
            Intent intentRegister = new Intent(MainActivity.this, kayitactivity.class);
            startActivity(intentRegister);
            finish();
        });
    }
}