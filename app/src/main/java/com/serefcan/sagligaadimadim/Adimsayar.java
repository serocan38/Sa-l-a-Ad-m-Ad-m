package com.serefcan.sagligaadimadim;

public class Adimsayar {

    private static final int ACCEL_RING_SIZE = 50;
    private static final int VEL_RING_SIZE = 10;

    // change this threshold according to your sensitivity preferences
    private static final float STEP_THRESHOLD = 50f;   //eşik değeri

    private static final int STEP_DELAY_NS = 250000000;  //nanosec

    private int accelRingCounter = 0;
    private float[] accelRingX = new float[ACCEL_RING_SIZE];
    private float[] accelRingY = new float[ACCEL_RING_SIZE];
    private float[] accelRingZ = new float[ACCEL_RING_SIZE];
    private int velRingCounter = 0;
    private float[] velRing = new float[VEL_RING_SIZE];
    private long lastStepTimeNs = 0;
    private float oldVelocityEstimate = 0; //hız

    private IStepListener listener;

    public void registerListener(IStepListener listener) {
        this.listener = listener;
    }


    public void step1(long timeNs){
        listener.step(timeNs);
        lastStepTimeNs = timeNs;
    }


    public void updateAccel(long timeNs, float x, float y, float z) {
        float[] currentAccel = new float[3]; //acceleration -> ivme
        currentAccel[0] = x;
        currentAccel[1] = y;
        currentAccel[2] = z;

        //50 lik eski veriler ile is yapiyor.
        // First step is to update our guess of where the global z vector is.
        accelRingCounter++;
        accelRingX[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[0];
        accelRingY[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[1];
        accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[2];

        float[] worldZ = new float[3];
        worldZ[0] = SensorFilter.sum(accelRingX) / Math.min(accelRingCounter, ACCEL_RING_SIZE);  //son 50 seferden ortalama alıp tahmin yspıyor
        worldZ[1] = SensorFilter.sum(accelRingY) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
        worldZ[2] = SensorFilter.sum(accelRingZ) / Math.min(accelRingCounter, ACCEL_RING_SIZE);

        float normalization_factor = SensorFilter.norm(worldZ);    //"normalize" etti

        worldZ[0] = worldZ[0] / normalization_factor;
        worldZ[1] = worldZ[1] / normalization_factor;
        worldZ[2] = worldZ[2] / normalization_factor;

        //eski 50 verinin normalize hali ile şimdiki x y z değerim arasındaki ilişkinin hesaplanması ve bir değer çıkarılması
        float currentZ = SensorFilter.dot(worldZ, currentAccel) - normalization_factor;
        velRingCounter++;
        velRing[velRingCounter % VEL_RING_SIZE] = currentZ;

        float velocityEstimate = SensorFilter.sum(velRing);    //hız tahmini yaptı.

        //yapılan hız tahmini eşik değerini aşıyorsa ve bir önceki hız tahminide eşik değerinden küçükse adım attı
        if (velocityEstimate > STEP_THRESHOLD && oldVelocityEstimate <= STEP_THRESHOLD
                && (timeNs - lastStepTimeNs > STEP_DELAY_NS)) {
            step1(timeNs);
        }

        oldVelocityEstimate = velocityEstimate;
    }
}

