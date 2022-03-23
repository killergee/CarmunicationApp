package com.carmunicationv01.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BluetoothConnections extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connections);


        // links xml with the code
        Button returnToMain2 = findViewById(R.id.return_Button2);
        // move to new page
        returnToMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // use below move between pages
                startActivity(new Intent(BluetoothConnections.this, MainActivity.class));
            }
        });
    }
}