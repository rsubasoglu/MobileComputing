package com.example.serkan.myapplication.Sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created on 01.04.2015.
 */
public class AccelerometerSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float x;
    private float y;

    public AccelerometerSensor(Object sensorService) {
        sensorManager = (SensorManager) sensorService;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // Many sensors return 3 values, one for each axis.
        x = event.values[2];
        y = event.values[1];
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
