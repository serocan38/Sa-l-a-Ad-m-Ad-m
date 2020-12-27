package com.serefcan.sagligaadimadim;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.serefcan.sagligaadimadim.model.AdimsayarModel;

import java.util.Calendar;
import java.util.Date;


public class AdimSayarServis extends Service implements SensorEventListener, IStepListener {
    private Adimsayar simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    public int numSteps;
    FirebaseUser fuser;
    FirebaseAuth fAuth;
    AdimsayarModel adimsayarModel = new AdimsayarModel();
    private DatabaseReference uRef;
    Date date = new Date();
    int sayac,toplam,kontrolSayac;
    int gun,gun1;
    int son,son2;
    boolean gunDegisti = false;
    String gunBilgisi,gunB,sGun;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        System.out.println("ACİLDİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİ");

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new Adimsayar();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(AdimSayarServis.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        fAuth = FirebaseAuth.getInstance();
        fuser=fAuth.getCurrentUser();
        uRef = FirebaseDatabase.getInstance().getReference("Adimsayar");
        gun=1;
       /* uRef.child(fuser.getUid()).child("1gun").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ( uRef.child(fuser.getUid()).child("1gun").child("adimsayisi") == null){
                    adimsayarModel.setAdimsayisi(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        uRef.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdimsayarModel model = dataSnapshot.getValue(AdimsayarModel.class);
                gun1=model.getSongun();
                uRef.child(fuser.getUid()).child(String.valueOf(gun1)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AdimsayarModel model1 = dataSnapshot.getValue(AdimsayarModel.class);
                        son2=model1.getAdimsayisi();
                        sayac = son2;
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

        uRef.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdimsayarModel model = dataSnapshot.getValue(AdimsayarModel.class);
                gun=model.getSongun();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        uRef.child(fuser.getUid()).child(String.valueOf(gun)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    adimsayarModel.setTarih(date.getTime());
                    uRef.child(fuser.getUid()).child(String.valueOf(gun)).setValue(adimsayarModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {







        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("KAPANDİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİİ");
    }

    @Override
    public void step(final long timeNs) {
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                new Intent(MainActivity.FILTER).putExtra("key", numSteps + sayac));


        Calendar calendar = Calendar.getInstance();
        int bugün = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", 0);
        int dün = sharedPreferences.getInt("day", 0);

        System.out.println("sayac: " + sayac);
        if (dün != bugün) {
            gunDegisti = true;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("day", bugün);
            editor.commit();
            gun++;
            sayac = 0;
            uRef.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    uRef.child(fuser.getUid()).child("songun").setValue(gun);
                    gunDegisti = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            uRef.child(fuser.getUid()).child(String.valueOf(gun)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    uRef.child(fuser.getUid()).child(String.valueOf(gun)).child("tarih").setValue(date.getTime());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



       /* final SharedPreferences sh = getSharedPreferences("mySharedP",MODE_PRIVATE);
                son = sh.getInt("intValue", 0);*/


      /*  uRef.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdimsayarModel model = dataSnapshot.getValue(AdimsayarModel.class);
                gun1=model.getSongun();
                uRef.child(fuser.getUid()).child(String.valueOf(gun1)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AdimsayarModel model1 = dataSnapshot.getValue(AdimsayarModel.class);
                        son2=model1.getAdimsayisi();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        sayac++;
        if (!gunDegisti) {
            uRef.child(fuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AdimsayarModel model = dataSnapshot.getValue(AdimsayarModel.class);
                    gun1 = model.getSongun();
                    uRef.child(fuser.getUid()).child(String.valueOf(gun1)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                uRef.child(fuser.getUid()).child(String.valueOf(gun1)).child("adimsayisi").setValue(sayac);
                            } else {
                                uRef.child(fuser.getUid()).child(String.valueOf(gun1)).child("adimsayisi").setValue(sayac);
                            }
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
    }
}
