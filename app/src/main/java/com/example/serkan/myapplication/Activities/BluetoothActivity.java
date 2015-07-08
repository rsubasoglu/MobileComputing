package com.example.serkan.myapplication.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.serkan.myapplication.R;
import com.example.serkan.myapplication.Views.MultiPlayerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created on 21.05.2015.
 */
public class BluetoothActivity extends Activity {

    private ListView mListView;

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mArrayAdapter;
    private String NAME = "Server Device";
    private UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private int REQUEST_ENABLE_BT = 1;
    private MultiPlayerView mpv;
    SensorManager sensorService;
    Activity activity;
    //AcceptThread acc; //Objekterstellung - Thread fuer BT Server Socket
    //private BluetoothServerSocket mmServerSocket;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_textview);

        mListView = (ListView) findViewById(R.id.listView);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            getPairedDevices();
            if(mBluetoothAdapter.startDiscovery())
                getDiscoveringDevices();

            sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            activity = this;

            // warte auf verbindungen in einem neuen thread
            Thread AcceptThread = new Thread(new AcceptThread(sensorService));
            AcceptThread.start();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                //Lese vom geklickten Gerat den Namen + die ID
                String str = mListView.getItemAtPosition(position).toString();

                //Speichere nur die ID
                String klickedDeviceID = str.substring(str.indexOf('\n')+1, str.indexOf("device")-1);
                String klickedDeviceTyp = str.substring(str.indexOf("device")+7, str.length());
                Log.e("klicked device typ", klickedDeviceTyp);
                Log.e("klicked device id", klickedDeviceID);

                if(klickedDeviceTyp.equals("paired")) {
                    /* HIER WERDEN NUR DIE GEKOPPELTEN GERATE EINGELESEN UND GEPRUFT */
                    Iterator<BluetoothDevice> iterator = mBluetoothAdapter.getBondedDevices().iterator();

                    while (iterator.hasNext()) {
                        BluetoothDevice selectedDevice = iterator.next();
                        //Log.e("iterator.getadress",selectedDevice.getAddress());
                        if (selectedDevice.getAddress().equals(klickedDeviceID)) {
                            //Log.e("n", "adressess equal");
                            Thread ConnectThread = new Thread(new ConnectThread(selectedDevice, sensorService));
                            ConnectThread.start();
                        }
                    }

                } else if (klickedDeviceTyp == "found") {
                    /* die IDs von gefundenen Geraten muss hier zur Uberprufung mit dem
                    geklickten Gerat prasent sein */
                }
            }
        });

    }

    public void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" + "device paired");
                Log.e("paired device name", device.getName());
            }
        }
        mListView.setAdapter(mArrayAdapter);
    }

    public void getDiscoveringDevices() {
        // Create a BroadcastReceiver for ACTION_FOUND
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" + "device found");
                    Log.e("discovered device name", device.getName());
                }else {
                    mArrayAdapter.add("no device found");
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        mListView.setAdapter(mArrayAdapter);
    }

    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;
        SensorManager sensorService;

        public AcceptThread(SensorManager sensorService) {
            this.sensorService = sensorService;

            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    Log.e("serversocket"," call accept()");
                    socket = mmServerSocket.accept();
                    /* This is a blocking call. It will return when either a connection has
                    been accepted or an exception has occurred. A connection is accepted only
                    when a remote device has sent a connection request with a UUID matching the
                    one registered with this listening server socket. When successful, accept() will
                    return a connected BluetoothSocket. */
                } catch (IOException e) {
                    Log.e(String.valueOf(e), " - fehler bei mmserversocket.accept()");
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
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
                        try {
                            myRunnable.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Do work to manage the connection (in a separate thread)
                    Thread connectedThread = new Thread(new ConnectedThread(socket, true));
                    connectedThread.start();
                    //manageConnectedSocket(socket);
                    cancel(); //close server
                    break;
                }
            }
        }

        // Will cancel the listening socket, and cause the thread to finish
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }


    /* Connecting as a client */

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        SensorManager sensorService;

        public ConnectThread(BluetoothDevice device, SensorManager sensorService) {
            this.sensorService = sensorService;

            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(mmSocket);

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

            Thread ConnectedThread = new Thread(new ConnectedThread(mmSocket, false));
            ConnectedThread.start();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


    /* Managing a Connection */

    private class ConnectedThread extends Thread {
        private static final int MESSAGE_READ = 0;
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private Handler mHandler;
        private boolean isServer;


        public ConnectedThread(BluetoothSocket socket, boolean isServer) {
            mmSocket = socket;
            this.isServer = isServer;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.e("bt","connected. data transfer started");
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
                BufferedReader in = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
                PrintWriter out = new PrintWriter(mmSocket.getOutputStream(), true);

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
                BufferedReader in = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
                PrintWriter out = new PrintWriter(mmSocket.getOutputStream(), true);

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

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    /*private class BluetoothConnection extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        byte[] buffer;

        // Unique UUID for this application, you may use different
        private final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

        public BluetoothConnection(BluetoothDevice device) {

            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;

            //now make the socket connection in separate thread to avoid FC
            Thread connectionThread  = new Thread(new Runnable() {

                @Override
                public void run() {
                    // Always cancel discovery because it will slow down a connection
                    mBluetoothAdapter.cancelDiscovery();

                    // Make a connection to the BluetoothSocket
                    try {
                        // This is a blocking call and will only return on a
                        // successful connection or an exception
                        mmSocket.connect();
                    } catch (IOException e) {
                        //connection to device failed so close the socket
                        try {
                            mmSocket.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            });

            connectionThread.start();

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
                buffer = new byte[1024];
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {

            try {
                int zahl = 0;
                String response = "";
                boolean ok = true;

                BufferedReader in = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
                PrintWriter out = new PrintWriter(mmSocket.getOutputStream(), true);

                // Keep listening to the InputStream while connected
                while (true) {
                    if (ok) {
                        /*if(mpv.isNewBalkAdded()) {
                            out.println("newBalk");
                            out.println(mpv.getNewBalkPosX());
                        }*/
    /*
                        out.println("test");
                        ok = false;
                    }
                    else {
                        response = in.readLine();
                        Log.e("n", response);
                        //zahl = Integer.valueOf(response);
                        //mpv.setRemoteBallX(zahl);
                        ok = true;
                    }
                }
            } catch (IOException e) {
                //an exception here marks connection loss
                //send message to UI Activity
            }
        }

        public void write(byte[] buffer) {
            try {
                //write the data to socket stream
                mmOutStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
*/
}
