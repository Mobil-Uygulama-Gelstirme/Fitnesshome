package com.example.fitnesshome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String[] baslik;
    String[] data1;
    String[] data2;
    int[] images;
    Context context;
    int isVisible;

    public  MyAdapter(Context ct, String[] s1, String[] s2, int[] img, int visibility){
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
        isVisible= visibility;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt1.setText(data1[position]);
        holder.txt2.setText(data2[position]);
        holder.imageView.setImageResource(images[position]);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Arrays.equals(data1, baslik))
                {
                    Intent intent = new Intent(context, antrenmanIcerikleri_activity.class);
                    intent.putExtra("data1", data1[position]);
                    context.startActivity(intent);
                }
            }
        });



        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Video_activity.class);
                intent.putExtra("move_name", data1[position]);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {return images.length;}



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt1, txt2;
        ImageView imageView;
        ConstraintLayout mainLayout;
        Button btn;


        public MyViewHolder(@NonNull  View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.Antreman_Programlari_txt);
            txt2 = itemView.findViewById(R.id.Aciklama_txt);
            imageView = itemView.findViewById(R.id.My_imageView);
            mainLayout = itemView.findViewById(R.id.mainlayout);
            btn = itemView.findViewById(R.id.btnvideo);

            baslik = itemView.getResources().getStringArray(R.array.Antreman_Programlari);

            if(isVisible==1){
                itemView.findViewById(R.id.btnvideo).setVisibility(View.VISIBLE);}
            else if (isVisible==0){
                itemView.findViewById(R.id.btnvideo).setVisibility(View.INVISIBLE);}

        }

    }
}
