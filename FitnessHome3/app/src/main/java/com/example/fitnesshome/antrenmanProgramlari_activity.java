package com.example.fitnesshome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class antrenmanProgramlari_activity extends AppCompatActivity {
    RecyclerView recyclerView;
    String[] AntrenmanProgramlari_isimleri;
    String[] Aciklamalar;
    int[] images ={R.drawable.postur,R.drawable.mobility,R.drawable.coretraining};
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antrenman_programlari);

        recyclerView = findViewById(R.id.rycview);

        AntrenmanProgramlari_isimleri = getResources().getStringArray(R.array.Antreman_Programlari);
        Aciklamalar = getResources().getStringArray(R.array.Aciklama);

        myAdapter = new MyAdapter(this, AntrenmanProgramlari_isimleri, Aciklamalar, images, 0);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}