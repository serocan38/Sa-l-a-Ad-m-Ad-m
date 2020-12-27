package com.serefcan.sagligaadimadim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serefcan.sagligaadimadim.model.KullaniciModel;

public class GirisBirinci extends AppCompatActivity {
    EditText yasET,boyET,kiloET,hedefKiloEtt;
    Button devamBtn;
    TextView uyarıTw;
    private RadioGroup radioGroup;
    private RadioButton radioSaglik,radioKilo;
    FirebaseUser fuser;
    FirebaseAuth fAuth;
    KullaniciModel kullaniciModel = new KullaniciModel();
    private DatabaseReference uRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_birinci);

        yasET=findViewById(R.id.yasET);
        boyET=findViewById(R.id.boyET);
        kiloET=findViewById(R.id.kiloET);
        radioGroup =  findViewById(R.id.radiooSex);
        radioSaglik =  findViewById(R.id.radiooSaglik);
        radioKilo =  findViewById(R.id.radiooKilo);
        devamBtn = findViewById(R.id.devamBtnn);
        uyarıTw = findViewById(R.id.uyarıTww);
        hedefKiloEtt = findViewById(R.id.hedefKiloEtt);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioKilo.isChecked()){
                    System.out.println("kilo seçili");
                    hedefKiloEtt.setVisibility(View.VISIBLE);


                }else{
                    System.out.println("sağlık seçili");
                    hedefKiloEtt.setVisibility(View.INVISIBLE);
                }
            }
        });

        FirebaseDatabase.getInstance().getReference("Adimsayar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseDatabase.getInstance().getReference("Adimsayar").child(fuser.getUid()).child("songun").setValue(1);
                FirebaseDatabase.getInstance().getReference("Adimsayar").child(fuser.getUid()).child("1").child("adimsayisi").setValue(0);
                FirebaseDatabase.getInstance().getReference("Adimsayar").child(fuser.getUid()).child("1").child("tarih").setValue(0);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        fAuth = FirebaseAuth.getInstance();
        fuser=fAuth.getCurrentUser();
        uRef = FirebaseDatabase.getInstance().getReference("Kullanicilar");



        devamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (radioKilo.isChecked()) {
                    if (hedefKiloEtt.getText().equals("")) {
                        uyarıTw.setVisibility(View.VISIBLE);
                        kullaniciModel.setMod("kilo");
                        kullaniciModel.setHedefkilo(Integer.parseInt(hedefKiloEtt.getText().toString()));
                        kullaniciOlustur();
                        FirebaseDatabase.getInstance().getReference("Adimsayar").child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FirebaseDatabase.getInstance().getReference("Adimsayar").child(fuser.getUid()).child("songun").setValue(1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);

                    }
                }else{
                    kullaniciModel.setMod("saglik");
                    kullaniciOlustur();
                    FirebaseDatabase.getInstance().getReference("Adimsayar").child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FirebaseDatabase.getInstance().getReference("Adimsayar").child(fuser.getUid()).child("songun").setValue(1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
    }

    public void kullaniciOlustur(){
        kullaniciModel.setId(fuser.getUid());
        kullaniciModel.setYas(Integer.parseInt(yasET.getText().toString()));
        kullaniciModel.setBoy(Integer.parseInt(boyET.getText().toString()));
        kullaniciModel.setKilo(Integer.parseInt(kiloET.getText().toString()));
        kullaniciModel.setAdimbuyuklugu(Integer.parseInt(boyET.getText().toString())*42/100);
        adSoyadAyir();
        uRef.child(fuser.getUid()).setValue(kullaniciModel);
    }

    public void adSoyadAyir(){
        String[] parts = fuser.getDisplayName().replaceAll("(“)|(”)|(\")","").split(" ");
        if(parts.length==1)
            kullaniciModel.setAdi(parts[0]);
        else if(parts.length==2) {
            kullaniciModel.setAdi(parts[0]);
            kullaniciModel.setSoyadi(parts[1]);
        }
        else {
            kullaniciModel.setAdi(parts[0]);
            int i=1;
            for (i=1;i<parts.length-1;i++){
                kullaniciModel.setAdi(kullaniciModel.getAdi()+" "+parts[i]);
            }
            kullaniciModel.setSoyadi(parts[i]);
        }
    }

}
