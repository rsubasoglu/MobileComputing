package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.util.Log;

import com.example.serkan.myapplication.R;


/**
 * Created on 19.03.2015.
 */
public class SekActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sek);

        TextView Vorname = (TextView) findViewById(R.id.Vorname);
        TextView Nachname = (TextView) findViewById(R.id.Nachname);
        Button backButton = (Button) findViewById(R.id.backButton);

        Intent i = getIntent();
        // Receiving the Data
        String vname = i.getStringExtra("Vorname");
        String nname = i.getStringExtra("Nachname");
        Log.e("zweite Activity", vname + "." + nname);

        // Displaying Received data
        Vorname.setText(vname);
        Nachname.setText(nname);

        // Binding Click event to Button
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });
    }
}