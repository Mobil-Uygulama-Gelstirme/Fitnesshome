package com.example.fitnesshome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class kullaniciactivity extends AppCompatActivity {

    private Button kullanicibtn;
    private EditText kad,ksoyad,kboy,kkilo;
    private CircleImageView kresim;
    private FirebaseAuth mAuth;
    private DatabaseReference mdb;
    private Uri imageuri=null;
    private Boolean IsCheck=false;
    private ProgressDialog kullaniciprogress;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullaniciactivity);

        Toolbar actionbarkullanici = findViewById(R.id.actionbarkullanici);
        setSupportActionBar(actionbarkullanici);
        getSupportActionBar().setTitle("KULLANICI BİLGİLERİ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kullanicibtn = (Button) findViewById(R.id.btnayarlar);
        kad =(EditText) findViewById(R.id.kullaniciadi);
        ksoyad =(EditText) findViewById(R.id.kullanicisoyadi);
        kboy =(EditText) findViewById(R.id.kullaniciboy);
        kkilo =(EditText) findViewById(R.id.kullanicikilo);
        kresim=(CircleImageView) findViewById(R.id.profilresim);

        kullaniciprogress= new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        String user_id= mAuth.getCurrentUser().getUid();

        mdb = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String user_ad=snapshot.child("Adi").getValue().toString();
                String user_soyad=snapshot.child("Soyadi").getValue().toString();
                String user_boy=snapshot.child("Boy").getValue().toString();
                String user_kilo=snapshot.child("Kilo").getValue().toString();
                String user_resim=snapshot.child("Fotograf").getValue().toString();
                imageuri=Uri.parse(user_resim);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.hesapsimge);

                Glide.with(kullaniciactivity.this).setDefaultRequestOptions(requestOptions).load(imageuri).into(kresim);

                kad.setText(user_ad);
                ksoyad.setText(user_soyad);
                kboy.setText(user_boy);
                kkilo.setText(user_kilo);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        kresim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(kullaniciactivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE},1);

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(kullaniciactivity.this);


            }
        });

        kullanicibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String guncelad=kad.getText().toString();
                String guncelsoyad=ksoyad.getText().toString();
                String guncelboy=kboy.getText().toString();
                String guncelkilo=kkilo.getText().toString();

                kullaniciprogress.setTitle("Güncelleniyor...");
                kullaniciprogress.setMessage("Bilgileriniz güncelleniyor.Lütfen bekleyiniz");
                kullaniciprogress.show();



                Map userUpdateMap=new HashMap();
                userUpdateMap.put("Adi",guncelad);
                userUpdateMap.put("Soyadi",guncelsoyad);
                userUpdateMap.put("Boy",guncelboy);
                userUpdateMap.put("Kilo",guncelkilo);
                mdb.updateChildren(userUpdateMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull  Task task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Güncelleme Başarılı",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Hata:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();
                kresim.setImageURI(imageuri);
                IsCheck=true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(getApplicationContext(),anaekranactivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
