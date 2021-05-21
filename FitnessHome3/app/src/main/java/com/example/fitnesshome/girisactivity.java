package com.example.fitnesshome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;


public class girisactivity extends AppCompatActivity
{
    private EditText txtmail, txtsifre;
    private Button girisbuton;

    private FirebaseAuth mAuth;
    private ProgressDialog loginProgress;


    public void init()
    {
        Toolbar actionbarlogin = findViewById(R.id.actionbarlogin);
        setSupportActionBar(actionbarlogin);
        getSupportActionBar().setTitle("GİRİŞ YAP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        txtmail = (EditText) findViewById(R.id.txtemailgiris);
        txtsifre = (EditText) findViewById(R.id.txtsifregiris);
        girisbuton = (Button) findViewById(R.id.btngiris);

        girisbuton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                String mail = txtmail.getText().toString();
                String sifre = txtsifre.getText().toString();
                if (!TextUtils.isEmpty(mail) || (!TextUtils.isEmpty(sifre)))
                {
                    loginProgress.setTitle("Oturum Açılıyor");
                    loginProgress.setMessage("Hesabınıza Giriş Yapılıyor.Lütfen bekleyin...");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    loginUser(mail, sifre);
                }

            }
        });
    }
    private void loginUser(String mail, String sifre)
    {
        mAuth.signInWithEmailAndPassword(mail,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    loginProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"Giriş Başarılı",Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(girisactivity.this,anaekranactivity.class);
                    startActivity(mainIntent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Giriş Başarısız:"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girisactivity);
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