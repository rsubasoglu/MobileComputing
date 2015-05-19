package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.serkan.myapplication.R;
import com.example.serkan.myapplication.Views.MultiPlayerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort = (EditText)findViewById(R.id.port);
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonClear = (Button)findViewById(R.id.clear);
        textResponse = (TextView)findViewById(R.id.response);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        buttonClear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }});
    }

    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener(){

                @Override
                public void onClick(View arg0) {
                    MyClientTask myClientTask = new MyClientTask(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()));
                    myClientTask.execute();
                    Object sensorService = getSystemService(Context.SENSOR_SERVICE);
                    multiPlayerView = new MultiPlayerView(activity, activity, sensorService);
                }};

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        Activity activity;

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                // receive
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                // send
                //OutputStream outputStream = socket.getOutputStream();
                //PrintStream printStream = new PrintStream(outputStream);

                int bytesRead;
                //while(true) {
                //    InputStream inputStream = socket.getInputStream();
                //    Log.e("n", "erster while");
    /*
     * notice:
     * inputStream.read() will block if no data return
     */
                    boolean ok = false;
                    int zahl = 0;
                    int zaehler = 0;

                    while (true) {
                        if(ok) {
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write();
                            PrintStream printStream = new PrintStream(outputStream);
                            printStream.print("123");
                            ok = false;
                            Log.e("n", "erste if");
                            outputStream.flush();
                        }
                        else {
                            Log.e("n", "else if");
                            InputStream inputStream = socket.getInputStream();
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                byteArrayOutputStream.write(buffer, 0, bytesRead);
                                response = byteArrayOutputStream.toString("UTF-8");
                                String temp = response.substring(0, 3);
                                zahl = Integer.valueOf(temp);
                                Log.e("n", "while -" + zahl);
                                response = "";
                                ok = true;
                                //socket.shutdownInput();

                                multiPlayerView.setRemoteBallX(zahl);
                                break;
                            }
                        }
                    }
                //}

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
