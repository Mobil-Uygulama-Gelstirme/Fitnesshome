package com.example.fitnesshome;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class antrenmanIcerikleri_activity extends AppCompatActivity {

    RecyclerView recyclerView2;
    MyAdapter myAdapter2;
    Context context=this;
    String programAdi;
    String[] posture_hareketleri;
    String[] posture_hareket_aciklamalari;
    String[] mobility_hareketleri;
    String[] mobility_aciklamalari;
    String[] core_hareketleri;
    String[] core_aciklamalari;
    TextView textView;

    int[] posture_images = {R.drawable.posturehareket1,R.drawable.posturehareket2,R.drawable.posturehareket3,
            R.drawable.posturehareket4, R.drawable.posturehareket5,R.drawable.posturehareket6,
            R.drawable.posturehareket7,R.drawable.posturehareket8};

    int[] mobility_images = {R.drawable.mobiilityhareket1,R.drawable.mobiilityhareket2,R.drawable.mobiilityhareket3,
            R.drawable.mobiilityhareket4,R.drawable.mobiilityhareket5,R.drawable.mobiilityhareket6};

    int[] core_images = {R.drawable.corehareket1,R.drawable.corehareket2,R.drawable.corehareket3,R.drawable.corehareket4,
            R.drawable.corehareket5,R.drawable.corehareket6,R.drawable.corerest};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antrenman_icerikleri);
        recyclerView2 = findViewById(R.id.recyclerView2);

        programAdi = getIntent().getStringExtra("data1");
        posture_hareketleri = getResources().getStringArray(R.array.Posture);
        posture_hareket_aciklamalari = getResources().getStringArray(R.array.Posture_Aciklama);
        mobility_hareketleri = getResources().getStringArray(R.array.Mobility);
        mobility_aciklamalari = getResources().getStringArray(R.array.Mobility_Aciklama);
        core_hareketleri = getResources().getStringArray(R.array.Core);
        core_aciklamalari = getResources().getStringArray(R.array.Core_Aciklama);

        if(programAdi.equals("Daily Posture Routine")){
            myAdapter2 = new MyAdapter(this, posture_hareketleri, posture_hareket_aciklamalari,posture_images,1);
        }
        else if(programAdi.equals("Mobility Routine")){
            myAdapter2 = new MyAdapter(this, mobility_hareketleri,mobility_aciklamalari,mobility_images,1);
        }
        else if(programAdi.equals("Core Training for beginners")){
            myAdapter2 = new MyAdapter(this,core_hareketleri,core_aciklamalari,core_images,1);
        }

        recyclerView2.setAdapter(myAdapter2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        textView=findViewById(R.id.textView);
        textView.setText(programAdi);
    }
}