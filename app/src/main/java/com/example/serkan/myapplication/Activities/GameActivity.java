package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.example.serkan.myapplication.R;
import com.example.serkan.myapplication.Views.DrawView1;
import com.example.serkan.myapplication.Views.SinglePlayerView;

/**
 * Created on 19.03.2015.
 * -------------------------------------------------------------------------------------------------
 * ---------------------------------- UNRELEVANT FUR UNSER PROJEKT ---------------------------------
 * -------------------------------------------------------------------------------------------------
 */
public class GameActivity extends Activity {
    DrawView1 drawView;
    //Highscore
    //private SharedPreferences gamePrefs;
    //public static final String GAME_PREFS = "ArithmeticFile";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //gamePrefs = getSharedPreferences(GAME_PREFS, 0);
        Object sensorService = getSystemService(Context.SENSOR_SERVICE);

        /*
        TextView ball = (TextView) findViewById(R.id.textView2);

        drawView = new DrawView1(this, sensorService);
        drawView.setBackgroundColor(Color.GREEN);
        setContentView(drawView);

        ball.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
        */

        SinglePlayerView spm = new SinglePlayerView(this, this, sensorService);
    }
}