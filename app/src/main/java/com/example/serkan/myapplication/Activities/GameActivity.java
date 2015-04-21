package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.serkan.myapplication.Views.DrawView1;
import com.example.serkan.myapplication.R;

/**
 * Created by Serkan on 19.03.2015.
 */
public class GameActivity extends Activity {
    DrawView1 drawView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Object sensorService = getSystemService(Context.SENSOR_SERVICE);

        TextView ball = (TextView) findViewById(R.id.textView2);

        drawView = new DrawView1(this, sensorService);
        drawView.setBackgroundColor(Color.GREEN);
        setContentView(drawView);

        ball.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}