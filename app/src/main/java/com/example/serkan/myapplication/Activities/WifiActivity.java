package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created on 09.05.2015.
 * quelle: http://android-er.blogspot.de/2014/02/android-sercerclient-example-server.html
 */
public class WifiActivity extends Activity {
    // socket variable fur die verbindung
    private ServerSocket serverSocket;
    private MultiPlayerView mpv;

    // variable furs wach bleiben
    private PowerManager.WakeLock wakeLock;

    private EditText editTextAddress, editTextPort;
    private Button buttonConnect, buttonClear;

    private Activity activity;
    private SensorManager sensorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        activity = this;

        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort = (EditText)findViewById(R.id.port);
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonClear = (Button)findViewById(R.id.clear);
        TextView textView = (TextView)findViewById(R.id.textView3);

        textView.setText("Your IP: " + getIpAddress());

        // wach bleiben
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "");
        wakeLock.acquire();

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        // aktiviere den sensor
        sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // warte auf verbindungen in einem neuen thread
        Thread socketServerThread = new Thread(new AcceptThread(sensorService));
        socketServerThread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                // verbindung beenden
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // wach bleiben beenden
        wakeLock.release();
    }

    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener(){

                @Override
                public void onClick(View arg0) {
                    // hole die eingegebenen daten (ip & port)
                    Thread ConnectThread = new Thread(new ConnectThread(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()), sensorService));
                    ConnectThread.start();
                }};

    // diese klasse erstellt einen socket und wartet auf verbindungen
    private class AcceptThread extends Thread {

        static final int SocketServerPORT = 8080;
        SensorManager sensorService;

        public AcceptThread(SensorManager sensorService) {
            this.sensorService = sensorService;
        }

        @Override
        public void run() {
            try {
                // erstellt einen socket mit festgelegtem port
                serverSocket = new ServerSocket(SocketServerPORT);

                //while (true) {

                //----------------------------------

                //------------------------------------

                    // programm wartet hier solange bis eine verbindung hergestellt wurde
                    Socket socket = serverSocket.accept();
                    Log.e("n", "" + socket.getRemoteSocketAddress());

                    // nach verbindungsaufbau wird ein neues thread gestartet
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // starte das spiel
                            mpv = new MultiPlayerView(activity, activity, sensorService, true);
                            // thread gibt bescheid das es fertig ist
                            synchronized (this) {
                                this.notify();
                            }
                        }
                    };

                    synchronized (myRunnable) {
                        // das thread "myRunnable" wird gestartet
                        activity.runOnUiThread(myRunnable);
                        // hier wird gewartet bis das thread fertig ist
                        myRunnable.wait();
                    }

                Thread ConnectedThread = new Thread(new ConnectedThread(socket, true));
                ConnectedThread.start();

                //}
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private class ConnectThread extends Thread {
        private String dstAddress;
        private int dstPort;
        private Socket socket;

        private SensorManager sensorService;

        public ConnectThread(String addr, int port, SensorManager sensorService) {
            this.dstAddress = addr;
            this.dstPort = port;
            this.sensorService = sensorService;
            socket = null;
        }

        public void run() {

            try {
                socket = new Socket(dstAddress, dstPort);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // nach verbindungsaufbau wird ein neues thread gestartet
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    // starte das spiel
                    mpv = new MultiPlayerView(activity, activity, sensorService, false);
                    // thread gibt bescheid das es fertig ist
                    synchronized (this) {
                        this.notify();
                    }
                }
            };

            synchronized (myRunnable) {
                // das thread "myRunnable" wird gestartet
                activity.runOnUiThread(myRunnable);
                // hier wird gewartet bis das thread fertig ist
                try {
                    myRunnable.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Thread ConnectedThread = new Thread(new ConnectedThread(socket, false));
            ConnectedThread.start();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) { }
        }
    }

    // diese klasse sendet und epfangt daten
    private class ConnectedThread extends Thread {

        private Socket socket;
        private boolean isServer;

        ConnectedThread(Socket socket, boolean isServer) {
            this.socket = socket;
            this.isServer = isServer;
        }

        @Override
        public void run() {
            if(isServer == true) {
                dataServer();
            }else dataClient();
        }

        public void dataServer() {
            try {
                // variable fur empfangene daten
                int zahl;
                String response;
                // wenn daten empfangen
                boolean ok = true;

                // variablen fur senden und empfangen
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // loop fur das kontinuierliche senden & empfangen
                while (true) {
                    // wenn daten empfangen
                    if (ok) {
                        // wenn ein neues balken generiert wurde
                        if(mpv.isNewBalkAdded()) {
                            // sende info das ein neues balken gesendet wird
                            out.println("newBalk");
                            // sende balken
                            out.println(mpv.getNewBalkPosX());
                        }
                        // sende koordinaten vom localen ball
                        out.println(mpv.getLocalBallX());
                        ok = false;
                    }
                    else {
                        // lese daten
                        response = in.readLine();
                        // umwandlung von string zu int
                        zahl = Integer.valueOf(response);
                        // setze remote ball position
                        mpv.setRemoteBallX(zahl);
                        ok = true;
                    }
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("e" ,"Something wrong! " + e.toString() + "\n");
            }
        }

        public void dataClient() {
            String response = "";

            try {
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
                        out.println(mpv.getLocalBallX());
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
                            mpv.setLocalNewBalkPosX(zahl);
                            response = in.readLine();
                        }
                        // umwandlung von string zu int
                        zahl = Integer.valueOf(response);
                        ok = true;
                        // setze remote ball position
                        mpv.setRemoteBallX(zahl);
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }
        }
    }

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

}
