package com.carmunicationv01.app;

import static androidx.core.content.ContextCompat.getSystemService;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Set;


public class BluetoothConnections extends AppCompatActivity {
    BluetoothDevice device;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    // Runs when the bluetooth page is opened
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connections);




        // Links xml with the code for button
        Button returnToMain2 = findViewById(R.id.return_Button2);
        // Move to new page
        returnToMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // use below move between pages
                startActivity(new Intent(BluetoothConnections.this, MainActivity.class));
            }
        });

        // Set-up bluetooth
        // Check the devices is supported
        // Bluetooth adapter declarition was here
        if (bluetoothAdapter == null) {
            //Code to inform if bluetooth is off/ unconnected
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Your device doesn't support bluetooth.");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        } else { // remove me potentially
            //Code to inform if bluetooth is off/ unconnected
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Your device does support bluetooth.");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }


        // Check if blue tooth is enabled and if not enable it
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            blueToothEnableARL.launch(enableBtIntent);
        }

        // Make this device discoverable to other devices
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        blueToothDiscoverable.launch(discoverableIntent);

        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter3.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mBroadcastReceiver3, filter3);





        // Connect to another device as a client
        Button connectToTheBlueToothServer = findViewById(R.id.bluthoothConnection_Button2);
        connectToTheBlueToothServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Connect to the device
                //new ConnectThread(device, bluetoothAdapter);
            }
        });

    }



    // Start enabling bluetooth
    ActivityResultLauncher<Intent> blueToothEnableARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 300) {
                        Intent data = result.getData();
                        Log. d("BlueTooth", "has been enabled" + data);
                    }
                }

            });

    // Make device discoverable
    ActivityResultLauncher<Intent> blueToothDiscoverable = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 1) {
                        Intent data = result.getData();
                        Log. d("BlueTooth", "device is discoverable for 5 minutes" + data);
                    }
                }

            });

    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Log. d("BlueTooth", "connected");
                    // Connect to the device

                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

                    Method getUuidsMethod = null;
                    try {
                        getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    ParcelUuid[] uuids = new ParcelUuid[0];
                    try {
                        uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    String theuuid = "";

                    for (ParcelUuid uuid: uuids) {
                        Log. d("BlueTooth", "UUID: " + uuid.getUuid().toString());
                        theuuid = uuid.getUuid().toString();
                    }

                    Log. d("BlueTooth", "disconnected222 " + theuuid);
                    new ConnectThread(device, bluetoothAdapter, theuuid);

                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log. d("BlueTooth", "disconnected");
                    unregisterReceiver(mBroadcastReceiver3);

                    break;
            }
        }
    };



}
















class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    BluetoothAdapter bluetoothAdapter;

    @SuppressLint("MissingPermission")
    public ConnectThread(BluetoothDevice device, BluetoothAdapter alreadyMadeBTAdapter, String uuid) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            UUID uuid2 = UUID.fromString(uuid);
            Log.d("BlueTooth", "Socket's create()------- " + uuid);

            BluetoothSocket sock = device.createRfcommSocketToServiceRecord(uuid2);
            tmp = sock;
            bluetoothAdapter = alreadyMadeBTAdapter;
        } catch (IOException e) {
            Log.d("Tag", "Socket's create() method failed ", e);

        }
        mmSocket = tmp;
    }

    @SuppressLint("MissingPermission")
    public void run() {
        bluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
            Log.d("BlueTooth", "2222222222222 ");
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.d("TAG", "Could not close the client socket ", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        //manageMyConnectedSocket(mmSocket); Dont need to use this yet - used for dont stuff after connection
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.d("TAG", "Could not close the client socket ", e);
        }
    }
}