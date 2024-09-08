package com.arabianmaps.compass.Compass_Compass_customCompass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

/* loaded from: classes2.dex */
public class Compass_A implements SensorEventListener {
    private static final String TAG = "Compass";
    private float azimuth;
    private float azimuthFix;
    Context context;
    private Sensor gsensor;
    private CompassListener listener;
    private Sensor msensor;
    private SensorManager sensorManager;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];

    /* renamed from: R */
    private float[] f177R = new float[9];

    /* renamed from: I */
    private float[] f176I = new float[9];
    public boolean isExit = false;

    /* loaded from: classes2.dex */
    public interface CompassListener {
        void onNewAzimuth(float f);
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public Compass_A(Context context) {
        this.context = context;
        this.sensorManager = (SensorManager) context.getSystemService("sensor");
        this.gsensor = this.sensorManager.getDefaultSensor(1);
        this.msensor = this.sensorManager.getDefaultSensor(2);
    }

    public void start() {
        this.sensorManager.registerListener(this, this.gsensor, 1);
        this.sensorManager.registerListener(this, this.msensor, 1);
        SensorManager sensorManager = this.sensorManager;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(3), 1);
        if (this.sensorManager.getDefaultSensor(3) != null) {
            Toast.makeText(this.context, "Sensor is supported", 0).show();
            this.isExit = false;
            return;
        }
        Toast.makeText(this.context, "Sensor is not supported", 0).show();
        this.isExit = true;
    }

    public void stop() {
        this.sensorManager.unregisterListener(this);
    }

    public void setAzimuthFix(float f) {
        this.azimuthFix = f;
    }

    public void resetAzimuthFix() {
        setAzimuthFix(0.0f);
    }

    public void setListener(CompassListener compassListener) {
        this.listener = compassListener;
    }

    @Override // android.hardware.SensorEventListener
    @SuppressLint({"SetTextI18n"})
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            if (sensorEvent.sensor.getType() == 1) {
                this.mGravity[0] = (this.mGravity[0] * 0.97f) + (sensorEvent.values[0] * 0.029999971f);
                this.mGravity[1] = (this.mGravity[1] * 0.97f) + (sensorEvent.values[1] * 0.029999971f);
                this.mGravity[2] = (this.mGravity[2] * 0.97f) + (sensorEvent.values[2] * 0.029999971f);
            }
            if (sensorEvent.sensor.getType() == 2) {
                this.mGeomagnetic[0] = (this.mGeomagnetic[0] * 0.97f) + (sensorEvent.values[0] * 0.029999971f);
                this.mGeomagnetic[1] = (this.mGeomagnetic[1] * 0.97f) + (sensorEvent.values[1] * 0.029999971f);
                this.mGeomagnetic[2] = (this.mGeomagnetic[2] * 0.97f) + (sensorEvent.values[2] * 0.029999971f);
                if (sensorEvent.sensor.getType() == 2) {
                    float f = sensorEvent.values[0];
                    float f2 = sensorEvent.values[1];
                    float f3 = sensorEvent.values[2];
                    String.format("%.0f", Double.valueOf(Math.sqrt((f * f) + (f2 * f2) + (f3 * f3))));
                }
            }
            if (SensorManager.getRotationMatrix(this.f177R, this.f176I, this.mGravity, this.mGeomagnetic)) {
                float[] fArr = new float[3];
                SensorManager.getOrientation(this.f177R, fArr);
                this.azimuth = (float) Math.toDegrees(fArr[0]);
                this.azimuth = ((this.azimuth + this.azimuthFix) + 360.0f) % 360.0f;
                if (this.listener != null) {
                    this.listener.onNewAzimuth(this.azimuth);
                }
            }
        }
    }
}
