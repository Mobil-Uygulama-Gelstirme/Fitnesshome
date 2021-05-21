package com.example.fitnesshome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import static android.widget.Toast.makeText;

public class kayitactivity extends AppCompatActivity {
    private EditText txtemail,txtsifre,txtad,txtsoyad,txtboy,txtkilo;
    private Button butonkayit;

    private FirebaseAuth mAuth;
    private ProgressDialog registerProgress;
    private DatabaseReference fitnesshomedb;


    public void init(){
        Toolbar actionbarRegister = findViewById(R.id.actionbarRegister);
        setSupportActionBar(actionbarRegister);
        Objects.requireNonNull(getSupportActionBar()).setTitle("HESAP OLUŞTUR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

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