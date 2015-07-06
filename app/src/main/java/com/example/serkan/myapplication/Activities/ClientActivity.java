package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.serkan.myapplication.R;
import com.example.serkan.myapplication.Views.MultiPlayerView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created on 09.05.2015.
 * quelle: http://android-er.blogspot.de/2014/02/android-sercerclient-example-client.html
 */
public class ClientActivity extends Activity{

    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;
    Activity activity = this;

    MultiPlayerView multiPlayerView;

    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort = (EditText)findViewById(R.id.port);
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonClear = (Button)findViewById(R.id.clear);
        textResponse = (TextView)findViewById(R.id.response);

        // Wach bleiben
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "");
        wakeLock.acquire();

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        buttonClear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }

    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener(){

                @Override
                public void onClick(View arg0) {
                    // hole die eingegebenen daten (ip & port)
                    MyClientTask myClientTask = new MyClientTask(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()));
                    myClientTask.execute();
                    // aktiviere den sensor
                    SensorManager sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                    // starte das spiel
                    multiPlayerView = new MultiPlayerView(activity, activity, sensorService, false);
                }};

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                // wenn daten empfangen
                boolean ok = false;
                // variable fur empfangene daten
                int zahl;

                // variablen fur senden und empfangen
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // loop fur das kontinuierliche senden & empfangen
                while (true) {
                    // wenn daten empfangen
                    if(ok) {
                        // sende koordinaten vom localen ball
                        out.println(multiPlayerView.getLocalBallX());
                        ok = false;
                    }
                    else {
                        // lese daten
                        response = in.readLine();
                        // wenn "newBalk" empfangen
                        if(response.equals("newBalk")) {
                            response = in.readLine();
                            zahl = Integer.valueOf(response);
                            // erstelle neuen balken
                            multiPlayerView.setLocalNewBalkPosX(zahl);
                            response = in.readLine();
                        }
                        // umwandlung von string zu int
                        zahl = Integer.valueOf(response);
                        ok = true;
                        // setze remote ball position
                        multiPlayerView.setRemoteBallX(zahl);
                    }
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textResponse.setText(response);
            super.onPostExecute(result);
        }

    }
}
