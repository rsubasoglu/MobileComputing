package com.example.serkan.myapplication.Drawables;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created on 01.04.2015.
 */
public class Ball implements SensorEventListener {

    private int x;
    private int y;
    private float r; // radius

    // sehr schlecht sollte unbedingt geandert werden!
    int MAX_X = 1080;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    public Ball(int x, int y, float r, SensorManager sensorManager) {
        this.x = x;
        this.y = y;
        this.r = r;
        if(sensorManager != null) {
            this.sensorManager = sensorManager;
            this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Many sensors return 3 values, one for each axis.
        updateBallPosition(event.values[2] * (-1));
    }

    /**
     * aktualisiert die position des balls
     * @param sensorX wert vom sensor
     */
    public void updateBallPosition(float sensorX) {
        // parameter x from accSensor:
        // 80 = left, 0 = center, -80 = right
        // get x parameter from sensor and convert it to percent
        float sensorXPercent = (sensorX + 40) * 100 / (80);
        if(sensorXPercent > 100)
            sensorXPercent = 100;
        else if(sensorXPercent < 0)
            sensorXPercent = 0;
        // calculate the percent number in display coord.
        int coordX = (int)(sensorXPercent * MAX_X / 100);
        // set ball position
        x = coordX;
    }
}
