package com.serefcan.sagligaadimadim;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class AyarlarEkran extends AppCompatActivity {

    EditText kiloEt,hedefKiloEt;
    private RadioGroup radioGroup;
    private RadioButton radioSaglik,radioKilo;
    TextView hedefKiloTw,uyarıTw;
    Button kaydetButon;
    FirebaseUser fuser;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar_ekran);
        fAuth = FirebaseAuth.getInstance();
        fuser=fAuth.getCurrentUser();
        kiloEt = findViewById(R.id.kiloET);
        hedefKiloEt = findViewById(R.id.hedefKiloEt);
        radioGroup = findViewById(R.id.radioSex);
        radioSaglik = findViewById(R.id.radioSaglik);
        radioKilo = findViewById(R.id.radioKilo);
        hedefKiloTw = findViewById(R.id.hedefKiloTw);
        kaydetButon = findViewById(R.id.kaydetButon);
        uyarıTw = findViewById(R.id.uyarımsgTw);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioKilo.isChecked()) {
                    System.out.println("kilo seçili");
                    hedefKiloEt.setVisibility(View.VISIBLE);
                    hedefKiloTw.setVisibility(View.VISIBLE);

                } else {
                    System.out.println("sağlık seçili");
                    hedefKiloEt.setVisibility(View.INVISIBLE);
                    hedefKiloTw.setVisibility(View.INVISIBLE);
                }
            }
        });


        kaydetButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hedefKiloEt.getVisibility()==View.VISIBLE) {
                    if (hedefKiloEt.getText().equals("")) {
                        uyarıTw.setVisibility(View.VISIBLE);
                        System.out.println("serooooooooooo");
                    }else{
                        kaydet("kilo");
                        FirebaseDatabase.getInstance().getReference("Kullanicilar").child(fuser.getUid()).child("hedefkilo").setValue(Integer.parseInt(hedefKiloEt.getText().toString()));
                    }
                }else{
                    kaydet("saglik");
                    System.out.println("seroooooooooookaydeeeeeeeeeeeeeeeeeeeeeee");

                }
                Toast.makeText(AyarlarEkran.this, "Başarıyla Kaydedildi", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void kaydet(String mod){
        FirebaseDatabase.getInstance().getReference("Kullanicilar").child(fuser.getUid()).child("mod").setValue(mod);

    }
}