package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.serkan.myapplication.R;
import com.example.serkan.myapplication.Views.MultiPlayerView;
import com.example.serkan.myapplication.Views.SinglePlayerView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created on 09.05.2015.
 * quelle: http://android-er.blogspot.de/2014/02/android-sercerclient-example-server.html
 */
public class MultiplayerActivity  extends Activity {
    TextView info, infoip, msg;
    String message = "";
    ServerSocket serverSocket;
    MultiPlayerView mpv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        info = (TextView) findViewById(R.id.info);
        infoip = (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);

        infoip.setText(getIpAddress());

        Thread socketServerThread = new Thread(new SocketServerThread(this));
        socketServerThread.start();

        Object sensorService = getSystemService(Context.SENSOR_SERVICE);
        mpv = new MultiPlayerView(this, this, sensorService);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 8080;
        int count = 0;
        Activity activity;

        public SocketServerThread(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                MultiplayerActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        info.setText("I'm waiting here: "
                                + serverSocket.getLocalPort());
                    }
                });

                while (true) {
                    Socket socket = serverSocket.accept();
                    count++;
                    Log.e("n", "" + socket.getRemoteSocketAddress());
                    message += "#" + count + " from " + socket.getInetAddress()
                            + ":" + socket.getPort() + "\n";

                    MultiplayerActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            msg.setText(message);
                        }
                    });

                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(socket, count, activity);
                    socketServerReplyThread.run();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;
        Activity activity;

        SocketServerReplyThread(Socket socket, int c, Activity activity) {
            hostThreadSocket = socket;
            cnt = c;
            this.activity = activity;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            String msgReply = "Hello from Android, you areTest #" + cnt;

            try {
                // send
                //outputStream = hostThreadSocket.getOutputStream();
                //PrintStream printStream = new PrintStream(outputStream);
                //printStream.print(msgReply);
                Log.e("n", "test");
                //printStream.close();

                // receive
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                //message += "replayed: " + msgReply + "\n";
                /*
                MultiplayerActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        msg.setText(message);
                    }
                });
*/
                int bytesRead = 0;
                int zaehler = 0;
                int zahl = 0;
                String response = "";
                boolean ok = true;

                /*
                while (true) {
                    if (ok) {
                        outputStream = hostThreadSocket.getOutputStream();
                        PrintStream printStream = new PrintStream(outputStream);
                        printStream.print(spm.getBallX());
                        ok = false;
                        Log.e("n", "erste if -" + spm.getBallX());
                        outputStream.flush();
                    }
                    else {
                        Log.e("n", "else if");
                        InputStream inputStream = hostThreadSocket.getInputStream();
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                            response = byteArrayOutputStream.toString("UTF-8");
                            String temp = response.substring(0, 3);
                            zahl = Integer.valueOf(temp);
                            Log.e("n", "while -" + zahl);
                            response = "";
                            ok = true;
                            //hostThreadSocket.shutdownInput();
                            break;
                        }
                    }
                    //Log.e("n", "" + spm.getBallX());
                }
                */

                BufferedReader in = new BufferedReader(new InputStreamReader(hostThreadSocket.getInputStream()));
                PrintWriter out = new PrintWriter(hostThreadSocket.getOutputStream(), true);

                while (true) {
                    if (ok) {
                        out.println(mpv.getLocalBallX());
                        ok = false;
                    }
                    else {
                        response = in.readLine();
                        zahl = Integer.valueOf(response);
                        mpv.setRemoteBallX(zahl);
                        ok = true;
                    }
                    //Log.e("n", "" + spm.getBallX());
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }
        }
    }
/*
            MultiplayerActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    msg.setText(message);
                }
            });
            */

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

}
