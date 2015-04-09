package com.example.serkan.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Serkan on 19.03.2015.
 */
public class GameActivity extends Activity {
    DrawView drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Object sensorService = getSystemService(Context.SENSOR_SERVICE);

        TextView ball = (TextView) findViewById(R.id.textView2);

        ImageView balken = new ImageView(this);
        balken.setImageResource(R.drawable.balken);

        drawView = new DrawView(this, sensorService);
        drawView.setBackgroundColor(Color.GREEN);
        setContentView(drawView);

        ball.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}