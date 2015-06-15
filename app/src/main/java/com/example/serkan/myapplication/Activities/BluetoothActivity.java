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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.serkan.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
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
    private String NAME;
    private UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");;
    private int REQUEST_ENABLE_BT = 1;

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
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                //Lese vom geklickten Gerät den Namen + die ID
                String str = mListView.getItemAtPosition(position).toString();

                //Speichere nur die ID
                String klickedDeviceID = str.substring(str.indexOf('\n')+1, str.indexOf("device")-1);
                String klickedDeviceTyp = str.substring(str.indexOf("device")+7, str.length());
                Log.e("klicked device typ", klickedDeviceTyp);
                Log.e("klicked device id", klickedDeviceID);

                if(klickedDeviceTyp == "paired") {
                    /* HIER WERDEN NUR DIE GEKOPPELTEN GERÄTE EINGELESEN UND GEPRÜFT */
                    Iterator<BluetoothDevice> iterator = mBluetoothAdapter.getBondedDevices().iterator();

                    while (iterator.hasNext()) {
                        BluetoothDevice selectedDevice = iterator.next();

                        if (selectedDevice.getAddress().equals(klickedDeviceID)) {
                            Log.e("n", "adressess equal");
                            BluetoothConnection bluetoothConnection = new BluetoothConnection(selectedDevice);
                        }
                    }
                } else if (klickedDeviceTyp == "found") {
                    /* die IDs von gefundenen Geräten muss hier zur Überprüfung mit dem geklickten Gerät präsent sein */
                }
            }
        });

    }

    /*
    public void test() {
        String name = "bluetoothserver";
        try {
            final BluetoothServerSocket btserver = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(name, MY_UUID);
            AsyncTask<Integer, Void, BluetoothSocket> acceptThread = new AsyncTask<Integer, Void, BluetoothSocket>() {
                @Override
                protected BluetoothSocket doInBackground(Integer... params) {

                    try {
                        socket = btserver.accept(params[0]*1000);
                        return socket;
                    } catch (IOException e) {
                        Log.d("BLUETOOTH", e.getMessage());
                    }

                    return null;
                }
                @Override
                protected void onPostExecute(BluetoothSocket result) {
                    if (result != null)
                        switchUI();
                }
            };
            acceptThread.execute(resultCode);
        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }
    }
    */

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

    private class BluetoothConnection extends Thread {
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

    /*
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
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
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                   // manageConnectedSocket(socket);
                    //mmServerSocket.close();
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
    }*/
}
