package com.example.serkan.myapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Serkan on 01.04.2015.
 */
public class AccelerometerSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float x;

    public AccelerometerSensor(Object sensorService) {
        sensorManager = (SensorManager) sensorService;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float newX = event.values[0];
        if(Math.abs(newX - x) > 0.25)
            x = event.values[0];
        // Do something with this sensor value.
    }

    public float getX() {
        return x;
    }
}
