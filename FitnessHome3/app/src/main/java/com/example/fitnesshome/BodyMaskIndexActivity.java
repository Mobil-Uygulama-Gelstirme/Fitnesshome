package com.example.fitnesshome;
//Beden kitle endeksi hesaplama sayfası
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class BodyMaskIndexActivity extends AppCompatActivity {

    private TextView txtBmi,txtBmiDesc,txtCalcBmi;          //Değişkenlerin tanımlanması
    private EditText edtBoy,edtKilo;
    private Button btnCalculate;
    private ProgressDialog progress;
    private FirebaseAuth mAuth;
    private DatabaseReference mdb;
    private String boy="",kilo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {            //Textview lere girilen değerlerin alınması
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_mask_index);
        mAuth=FirebaseAuth.getInstance();
        txtBmi = findViewById(R.id.txtBmi);
        txtBmiDesc = findViewById(R.id.txtBmiDesc);
        txtCalcBmi = findViewById(R.id.txtCalcBmi);
        edtBoy = findViewById(R.id.edtBoy);
        edtKilo = findViewById(R.id.edtKilo);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(v -> {
            if(edtBoy.getText().toString().isEmpty() || edtKilo.getText().toString().isEmpty()){        //Girilen değerlerin boş olup olmadığı kontrol edilir.
                Toast.makeText(getApplicationContext(), "Hesaplama yapmak için boy ve kilo bilgisi giriniz", Toast.LENGTH_LONG).show();
            } else {
                double calcBmi =calculateBmi(edtBoy.getText().toString(),edtKilo.getText().toString());
                txtCalcBmi.setText(String.format("VKİ : %.2f - ",calcBmi)+getBmiClassification(calcBmi));
            }

        });

        progress = new ProgressDialog(this);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        mdb = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mdb.addValueEventListener(new ValueEventListener() {        //Veritabanındaki değerlerin okunması
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                 boy= (String) snapshot.child("Boy").getValue();
                 kilo= (String) snapshot.child("Kilo").getValue();
                 setDefaultUserBmi();
                 progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    private double calculateBmi(String _boy,String _kilo) {     //Beden kitle endeksi hesaplaması

        double boyDouble = Double.parseDouble(_boy);
        double kiloDouble = Double.parseDouble(_kilo);
        double boy2=(boyDouble/100)*(boyDouble/100);
        return kiloDouble/boy2;
    }

    private void setDefaultUserBmi(){
        if ((boy==null || boy=="") && (kilo==null || kilo=="")) {
            finish();
        }
        double bmi=calculateBmi(boy,kilo);
        txtBmi.setText(String.format("VKİ : %.2f",bmi));
        txtBmiDesc.setText(getBmiClassification(bmi));
    }

    private String getBmiClassification(double bmi){ //Kontrollere göre değerlerin karşılığı bulunmuştur.
        if (bmi<18.5){
            //Zayıf
            return getString(R.string.faz1_bmi);
        } else if(18.5<=bmi && bmi<=24.9){
            //Normal Kilolu
            return getString(R.string.faz2_bmi);
        } else if(25.0<=bmi && bmi<=29.9){
            //Fazla Kilolu
            return getString(R.string.faz3_bmi);
        } else if(30.0<=bmi && bmi<=39.9){
            //Obez
            return getString(R.string.faz4_bmi);
        }
        else {
            //ileri derece obez
            return getString(R.string.faz5_bmi);
        }
    }
}