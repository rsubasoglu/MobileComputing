package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.example.serkan.myapplication.R;
import com.example.serkan.myapplication.Views.SinglePlayerView;

/**
 * Created on 19.03.2015.
 * -------------------------------------------------------------------------------------------------
 * ---------------------------------- UNRELEVANT FUR UNSER PROJEKT ---------------------------------
 * -------------------------------------------------------------------------------------------------
 */
public class GameActivity extends Activity {
    //Highscore
    //private SharedPreferences gamePrefs;
    //public static final String GAME_PREFS = "ArithmeticFile";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SensorManager sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        SinglePlayerView spm = new SinglePlayerView(this, this, sensorService);
    }
}