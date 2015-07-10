package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.util.Log;

import com.example.serkan.myapplication.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Die benötigte XML-Layout Datei:
        setContentView(R.layout.activity_main);

        // Zuweisung der XML Objekte an unsere Variabeln
        Button gameButton = (Button) findViewById(R.id.gameButton);
        Button multiplayerButton = (Button) findViewById(R.id.multiplayerButton);
        Button bluetoothButton = (Button) findViewById(R.id.bluetoothButton);

        gameButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), GameActivity.class);

                //Intent mit den Daten füllen

                // Log schreiben für Logausgabe
                Log.e("n", "wechsel zu Game");

                // Intent starten und zur zweiten Activity wechseln
                startActivity(nextScreen);

            }
        });

        multiplayerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), MultiplayerActivity.class);

                // Log schreiben für Logausgabe
                Log.e("n", "wechsel zu Multiplayer");

                // Intent starten und zur zweiten Activity wechseln
                startActivity(nextScreen);
            }
        });

        bluetoothButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), BluetoothActivity.class);

                // Log schreiben für Logausgabe
                Log.e("n", "wechsel zu Bluetooth");

                // Intent starten und zur zweiten Activity wechseln
                startActivity(nextScreen);
            }
        });
    }
}