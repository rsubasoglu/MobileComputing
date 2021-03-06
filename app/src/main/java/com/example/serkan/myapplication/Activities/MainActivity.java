package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.util.Log;

import com.example.serkan.myapplication.R;

public class MainActivity extends Activity {

    // Anlegen der Variabeln
    EditText inputVorname;
    EditText inputNachname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Die benötigte XML-Layout Datei:
        setContentView(R.layout.activity_main);

        // Zuweisung der XML Objekte an unsere Variabeln
        inputVorname = (EditText) findViewById(R.id.Vorname);
        inputNachname = (EditText) findViewById(R.id.Nachname);
        Button weiterButton = (Button) findViewById(R.id.submitButton);
        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        Button gameButton = (Button) findViewById(R.id.gameButton);
        Button rankingButton = (Button) findViewById(R.id.rankingButton);
        Button sensorButton = (Button) findViewById(R.id.sensorButton);
        Button multiplayerButton = (Button) findViewById(R.id.multiplayerButton);
        Button clientButton = (Button) findViewById(R.id.clientButton);

        // ClickListener implementieren für den Button zum Wechsel der Activity
        weiterButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), SekActivity.class);

                //Intent mit den Daten füllen
                nextScreen.putExtra("Vorname", inputVorname.getText().toString());
                nextScreen.putExtra("Nachname", inputNachname.getText().toString());

                // Log schreiben für Logausgabe
                Log.e("n", inputVorname.getText()+"."+ inputNachname.getText());

                // Intent starten und zur zweiten Activity wechseln
                startActivity(nextScreen);

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);

                //Intent mit den Daten füllen

                // Log schreiben für Logausgabe
                Log.e("n", "wechsel zu Settings");

                // Intent starten und zur zweiten Activity wechseln
                startActivity(nextScreen);

            }
        });

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

        rankingButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), RankingActivity.class);

                //Intent mit den Daten füllen

                // Log schreiben für Logausgabe
                Log.e("n", "wechsel zu Ranking");

                // Intent starten und zur zweiten Activity wechseln
                startActivity(nextScreen);

            }
        });

        sensorButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), SensorActivity.class);

                //Intent mit den Daten füllen

                // Log schreiben für Logausgabe
                Log.e("n", "wechsel zu Sensor");

                // Intent starten und zur zweiten Activity wechseln
                startActivity(nextScreen);

            }
        });

        multiplayerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), MultiplayerActivity.class);

                //Intent mit den Daten füllen

                // Log schreiben für Logausgabe
                Log.e("n", "wechsel zu Multiplayer");

                // Intent starten und zur zweiten Activity wechseln
                startActivity(nextScreen);

            }
        });

        clientButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), ClientActivity.class);

                //Intent mit den Daten füllen

                // Log schreiben für Logausgabe
                Log.e("n", "wechsel zu Client");

                // Intent starten und zur zweiten Activity wechseln
                startActivity(nextScreen);

            }
        });
    }
}