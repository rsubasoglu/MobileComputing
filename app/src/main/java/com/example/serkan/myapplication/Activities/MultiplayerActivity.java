package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.example.serkan.myapplication.R;
import com.example.serkan.myapplication.Views.MultiPlayerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created on 09.05.2015.
 * quelle: http://android-er.blogspot.de/2014/02/android-sercerclient-example-server.html
 */
public class MultiplayerActivity  extends Activity {
    // socket variable fur die verbindung
    ServerSocket serverSocket;
    MultiPlayerView mpv;

    // variable furs wach bleiben
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        // wach bleiben
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "");
        wakeLock.acquire();

        // aktiviere den sensor
        SensorManager sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // warte auf verbindungen in einem neuen thread
        Thread socketServerThread = new Thread(new SocketServerThread(this, sensorService));
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

    // diese klasse erstellt einen socket und wartet auf verbindungen
    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 8080;
        Activity activity;
        SensorManager sensorService;

        public SocketServerThread(Activity activity, SensorManager sensorService) {
            this.activity = activity;
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

                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket, activity);
                    socketServerReplyThread.run();

                //}
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // diese klasse sendet und epfangt daten
    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        Activity activity;

        SocketServerReplyThread(Socket socket, Activity activity) {
            hostThreadSocket = socket;
            this.activity = activity;
        }

        @Override
        public void run() {

            try {
                // variable fur empfangene daten
                int zahl;
                String response;
                // wenn daten empfangen
                boolean ok = true;

                // variablen fur senden und empfangen
                BufferedReader in = new BufferedReader(new InputStreamReader(hostThreadSocket.getInputStream()));
                PrintWriter out = new PrintWriter(hostThreadSocket.getOutputStream(), true);

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
    }

    /*
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
                        ip += "SiteLocalAddress: "
                                + inetAddress.getHostAddress() + "\n";
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
    */

}
