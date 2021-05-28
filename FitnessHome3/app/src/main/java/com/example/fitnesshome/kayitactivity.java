package com.example.fitnesshome;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Objects;

import static android.widget.Toast.makeText;

public class kayitactivity extends AppCompatActivity {
    private EditText txtemail,txtsifre,txtad,txtsoyad,txtboy,txtkilo;
    private Button butonkayit;
    private ImageView avatar;  //resim eklenecek nesne tanımlandı
    private FirebaseAuth mAuth;
    private ProgressDialog registerProgress;
    private DatabaseReference fitnesshomedb;
    private StorageReference storageRef ;
    private Bitmap imageBitmap;  //Telefonun içinde bulunan resim dosyasını çekip ImageView de göstermek için


    public void init(){
        Toolbar actionbarRegister = findViewById(R.id.actionbarRegister);
        setSupportActionBar(actionbarRegister);
        Objects.requireNonNull(getSupportActionBar()).setTitle("HESAP OLUŞTUR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();  //veritabanına fotoğraf, video vs depolamak
        storageRef = storage.getReference();

        registerProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        avatar = findViewById(R.id.avatar);
        txtemail=(EditText) findViewById(R.id.txtemail);
        txtsifre= (EditText) findViewById(R.id.txtsifre);
        txtad= (EditText) findViewById(R.id.txtad);
        txtsoyad= (EditText) findViewById(R.id.txtsoyad);
        txtboy= (EditText) findViewById(R.id.txtboy);
        txtkilo= (EditText) findViewById(R.id.txtkilo);
        butonkayit = (Button) findViewById(R.id.btnkayit);

        butonkayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtemail.getText().toString();
                String sifre= txtsifre.getText().toString();
                String ad = txtad.getText().toString();
                String soyad= txtsoyad.getText().toString();
                String boy = txtboy.getText().toString();
                String kilo= txtkilo.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(sifre) || !TextUtils.isEmpty(ad) || !TextUtils.isEmpty(soyad) || !TextUtils.isEmpty(boy) || !TextUtils.isEmpty(kilo)) {
                    registerProgress.setTitle("Kaydediliyor");
                    registerProgress.setMessage("Hesabınız Oluşturuluyor.Lütfen bekleyin...");
                    registerProgress.setCanceledOnTouchOutside(false);
                    registerProgress.show();
                    createNewAccount(email,sifre,ad,soyad,boy,kilo);

                }
            }
        });

        avatar.setOnClickListener(v -> {   //Veritabanına kaydedilecek fotoğraf için kamera izni
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.CAMERA },100);
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera,100);
            }

        });
    }


    @Override  //Kameradan activity e geri dönme
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==100){
            if (data.hasExtra("data")){
                Object object = data.getExtras().get("data");
                imageBitmap = (Bitmap) object;
                avatar.setImageBitmap(imageBitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createNewAccount(String email, String sifre, String ad, String soyad, String boy, String kilo) {

        mAuth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    String user_id=mAuth.getCurrentUser().getUid();
                    fitnesshomedb= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                    HashMap<String,String>userMap= new HashMap<>();
                    userMap.put("Email",email);
                    userMap.put("Sifre",sifre);
                    userMap.put("Adi",ad);
                    userMap.put("Soyadi",soyad);
                    userMap.put("Boy",boy);
                    userMap.put("Kilo",kilo);
                    userMap.put("Fotograf","default");
                    fitnesshomedb.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                uploadImage(user_id+".jpg");
                                registerProgress.dismiss();
                                Intent girisIntent = new Intent(kayitactivity.this, girisactivity.class);
                                startActivity(girisIntent);
                                finish();
                            }
                        }
                    });

                }
                else{
                    registerProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"Hata:"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void uploadImage(String imageName){  //Fotoğraf yükle fonksiyonu
        StorageReference ref = storageRef.child(imageName);
        if (imageBitmap!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            if (data!=null){
                UploadTask uploadTask = ref.putBytes(data);
                uploadTask.addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    Log.d("failure","upload");
                }).addOnSuccessListener(taskSnapshot -> {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    Log.d("success","upload");
                });
            }
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayitactivity);
        init();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}