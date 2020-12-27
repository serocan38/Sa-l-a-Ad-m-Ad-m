package com.serefcan.sagligaadimadim;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.serefcan.sagligaadimadim.model.AdimsayarModel;
import com.serefcan.sagligaadimadim.model.KullaniciModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView adimSayarTw,adimSayarBilgiTw,yakilanKaloriTw,yurunenKmTw,rekorTw,son1Tw,son2Tw,son3Tw,son4Tw,son5Tw,son6Tw,son7Tw,modTf,oneriTw;
    private ImageView ayarlarButon;
    private Adimsayar simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    AdimsayarModel adimsayarModel = new AdimsayarModel();
    FirebaseUser fuser;
    FirebaseAuth fAuth;
    private DatabaseReference uRef,kRef;
    int sayac,adimBuyuklugu;
    double yk,yakilanKalori;
    double yurunenKm;
    int buyuk=0;
    Calendar cal;
    ArrayList<Integer> list = new ArrayList<Integer>();

    String bugün,onceBir,onceIki,onceUc,onceDort,onceBes,oncealti,onceYedi;
    boolean serviscalisti=false;
    public static final String FILTER = "just.a.filter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();
        fuser=fAuth.getCurrentUser();
        uRef = FirebaseDatabase.getInstance().getReference("Adimsayar");
        kRef = FirebaseDatabase.getInstance().getReference("Kullanicilar");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new Adimsayar();
        adimSayarTw = findViewById(R.id.adimSayarTw);
        adimSayarBilgiTw = findViewById(R.id.adimSayarBilgiTw);
        yakilanKaloriTw = findViewById(R.id.yakilanKaloriTw);
        yurunenKmTw = findViewById(R.id.yurunenKmTw);
        rekorTw = findViewById(R.id.rekorTw);
        son1Tw = findViewById(R.id.son1Tw);
        son2Tw = findViewById(R.id.son2Tw);
        son3Tw = findViewById(R.id.son3Tw);
        son4Tw = findViewById(R.id.son4Tw);
        son5Tw = findViewById(R.id.son5Tw);
        son6Tw = findViewById(R.id.son6Tw);
        son7Tw = findViewById(R.id.son7Tw);
        modTf = findViewById(R.id.modTf);
        oneriTw = findViewById(R.id.oneriTw);


        kilooneriHesapla();
        ayarlarButon = findViewById(R.id.ayarlarButon);
        ayarlarButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,AyarlarEkran.class);
                startActivity(in);
            }
        });

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {

            case Calendar.SUNDAY:
                System.out.println("serocan : : : : : " + "Pazar");
                bugün="Pazar";
                break;
            case Calendar.MONDAY:
                System.out.println("serocan : : : : : " + "Pazartesi");
                bugün="Pazartesi";
                break;
            case Calendar.TUESDAY:
                System.out.println("serocan : : : : : " + "Salı");
                bugün="Salı";
                break;
            case Calendar.WEDNESDAY:
                System.out.println("serocan : : : : : " + "Çarşamba");
                bugün="Çarşamba";
                break;
            case Calendar.THURSDAY:
                System.out.println("serocan : : : : : " + "Perşembe");
                bugün="Perşembe";
                break;
            case Calendar.FRIDAY:
                System.out.println("serocan : : : : : " + "Cuma");
                bugün="Cuma";
                break;
            case Calendar.SATURDAY:
                System.out.println("serocan : : : : : " + "Cumartesi");
                bugün="Cumartesi";
                break;
        }

        list = getIntent().getIntegerArrayListExtra("gunlukadimsayisi");
        System.out.println("LİSTEEEEEEEEEEEEEEEEEEEEEEEEEEE : "+list);


        if (list!=null) {
            if (bugün.equals("Pazartesi")){
                onceBir="Pazar";
                onceIki="Cumartesi";
                onceUc="Cuma";
                onceDort="Perşembe";
                onceBes="Çarşamba";
                oncealti="Salı";
                onceYedi=bugün;
            }else if (bugün.equals("Salı")){
                onceBir="Pazartesi";
                onceIki="Pazar";
                onceUc="Cumartesi";
                onceDort="Cuma";
                onceBes="Perşembe";
                oncealti="Çarşamba";
                onceYedi=bugün;
            }else if (bugün.equals("Çarşamba")){
                onceBir="Salı";
                onceIki="Pazartesi";
                onceUc="Pazar";
                onceDort="Cumartesi";
                onceBes="Cuma";
                oncealti="Perşembe";
                onceYedi=bugün;
            }
            else if (bugün.equals("Perşembe")){
                onceBir="Çarşamba";
                onceIki="Salı";
                onceUc="Pazartesi";
                onceDort="Pazar";
                onceBes="Cumartesi";
                oncealti="Cuma";
                onceYedi=bugün;
            }else if (bugün.equals("Cuma")){
                onceBir="Perşembe";
                onceIki="Çarşamba";
                onceUc="Salı";
                onceDort="Pazartesi";
                onceBes="Pazar";
                oncealti="Cumartesi";
                onceYedi=bugün;
            }else if (bugün.equals("Cumartesi")){
                onceBir="Cuma";
                onceIki="Perşembe";
                onceUc="Çarşamba";
                onceDort="Salı";
                onceBes="Pazartesi";
                oncealti="Pazar";
                onceYedi=bugün;
            }else{
                onceBir="Cumartesi";
                onceIki="Cuma";
                onceUc="Perşembe";
                onceDort="Çarşamba";
                onceBes="Salı";
                oncealti="Pazartesi";
                onceYedi=bugün;
            }

            son1Tw.setText("" + list.get(0)+"\n"+onceBir);
            son2Tw.setText("" + list.get(1)+"\n"+onceIki);
            son3Tw.setText("" + list.get(2)+"\n"+onceUc);
            son4Tw.setText("" + list.get(3)+"\n"+onceDort);
            son5Tw.setText("" + list.get(4)+"\n"+onceBes);
            son6Tw.setText("" + list.get(5)+"\n"+oncealti);
            son7Tw.setText("" + list.get(6)+"\n"+onceYedi);

        }





       oneriTw.setText(""+saglikoneriHesapla());


        uRef.child(fuser.getUid()).child("songun").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int son=  Integer.parseInt(dataSnapshot.getValue().toString());
                int hangiGun;
                if (son<8) {
                    hangiGun = son;
                }else{
                    hangiGun=7;
                }

                adimSayarBilgiTw.setText("Son "+hangiGun+" Gündeki Adım Sayıları");
                for (int i=1 ; i<=son ; i++) {

                    uRef.child(fuser.getUid()).child(""+i).child("adimsayisi").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                            int b=Integer.parseInt(dataSnapshot.getValue().toString());
                            if (buyuk==0 ||  b>buyuk){
                                buyuk = Integer.parseInt(dataSnapshot.getValue().toString());
                                rekorTw.setText(""+buyuk);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                    modTf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent a=new Intent(MainActivity.this,AyarlarEkran.class);
                            startActivity(a);
                        }
                    });

                kRef.child(fuser.getUid()).child("mod").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        modTf.setText("Uygulama modu : "+dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                uRef.child(fuser.getUid()).child(String.valueOf(dataSnapshot.getValue())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AdimsayarModel model1 = dataSnapshot.getValue(AdimsayarModel.class);
                        sayac = model1.getAdimsayisi();
                        adimSayarTw.setText("" + sayac);
                        yakilanKalori=  0.04*sayac;
                        yakilanKaloriTw.setText(""+yakilanKalori+"\nkcal");

                        kRef.child(fuser.getUid()).child("adimbuyuklugu").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                adimBuyuklugu=Integer.parseInt(dataSnapshot.getValue().toString());
                                yk=(adimBuyuklugu*sayac);
                                yurunenKm=yk/100000;
                                String km=new DecimalFormat("###.##").format(yurunenKm);
                                yurunenKmTw.setText(km+"\nKilometre");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       serviscalisti = isMyServiceRunning(AdimSayarServis.class);
        if (serviscalisti==false) {
            startService(new Intent(getApplicationContext(), AdimSayarServis.class));
            serviscalisti=true;
        }

    }

    private boolean isMyServiceRunning(Class<AdimSayarServis> AdimSayarServis) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AdimSayarServis.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {



        super.onStart();
    }

    @Override
    protected void onDestroy() {
     /*   SharedPreferences sharedPref = getSharedPreferences("mySharedP",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("intValue",sayac); //int değer kayıt eklemek için kullanıyoruz.
        editor.commit(); //Kayıt.

*/


        super.onDestroy();
    }

    public int saglikoneriHesapla(){
        list = getIntent().getIntegerArrayListExtra("gunlukadimsayisi");
        int ort=0;
        if(list!=null) {
            System.out.println("serooooooooo" + list);
            int listeboyutu = list.size();
            int toplam = 0;
            for (int i = 0; i < listeboyutu; i++) {
                toplam += list.get(i);
                System.out.println("serooooooooo" + i + " "+toplam);
                System.out.println("seroooooooooss" + listeboyutu);


            }
            ort = toplam / listeboyutu;
        }

        if (ort<5000){
            return 5000;
        }else if (ort>15000){
            return 15000;
        }
        return (int) (Math.floor((ort + 100/2) / 100) * 100)+1000;
}



    public int kilooneriHesapla(){
        list = getIntent().getIntegerArrayListExtra("gunlukadimsayisi");
        int ort=0;
        if(list!=null) {
            System.out.println("serooooooooo" + list);
            int listeboyutu = list.size();
            int toplam = 0;
            for (int i = 0; i < listeboyutu; i++) {
                toplam += list.get(i);
                System.out.println("serooooooooo" + i + " "+toplam);
                System.out.println("seroooooooooss" + listeboyutu);


            }



            kRef.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    KullaniciModel kullaniciModel = dataSnapshot.getValue(KullaniciModel.class);
                    float ay = 12;
                    float vermekIstenenKılo = kullaniciModel.getKilo()-kullaniciModel.getHedefkilo();
                    //Günlük olarak ekstra 1000 kalori yakmak haftalık 1 kiloya denk gelmekte
                    System.out.println("seroooooooooooo : "+vermekIstenenKılo);
                    if (vermekIstenenKılo<5){
                        ay=12;
                    }else if (vermekIstenenKılo<10){
                        ay=24;
                    }else if (vermekIstenenKılo<15){
                        ay=36;
                    }else{
                    }


                    float y = (vermekIstenenKılo/ay*7000/7*20)+3000;

                    System.out.println("sero  "+y);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

        if (ort<5000){
            return 5000;
        }else if (ort>15000){
            return 15000;
        }
        return (int) (Math.floor((ort + 100/2) / 100) * 100)+1000;
    }


}