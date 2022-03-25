package com.carmunicationv01.app;

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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class BluetoothConnections extends AppCompatActivity {
    BluetoothDevice device;

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
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
        }


        // Check if blue tooth is enabled and if not enable it
        // - add this in later for now could asks user to turn bluetooth on.

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        // Set the device as discoverable for 5 minutes
        // Come back to this.

        // Connect to another device as a client
        Button connectToTheBlueToothServer = findViewById(R.id.bluthoothConnection_Button2);
        connectToTheBlueToothServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Connect to the device
                new ConnectThread(device, bluetoothAdapter);

            }
        });

    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //String deviceName = device.getName();
                String deviceName = "tester"; // Use this untill the line above is fixed.
                String deviceHardwareAddress = device.getAddress(); // MAC address

            }
        }
    };

    // Unregister the receiver object when it is finished being used.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}

class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    BluetoothAdapter bluetoothAdapter;

    @SuppressLint("MissingPermission")
    public ConnectThread(BluetoothDevice device, BluetoothAdapter alreadyMadeBTAdapter) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            ParcelUuid[] idArray = device.getUuids();
            int i = 0;
            java.util.UUID uuidYouCanUse = java.util.UUID.fromString(idArray[i].toString());
            tmp = device.createRfcommSocketToServiceRecord(uuidYouCanUse);
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