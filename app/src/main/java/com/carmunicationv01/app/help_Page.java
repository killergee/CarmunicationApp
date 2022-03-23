package com.carmunicationv01.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class help_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);


        // links xml with the code
        Button returnToMain = findViewById(R.id.return_Button);
        // move to new page
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // use below move between pages
                startActivity(new Intent(help_Page.this, MainActivity.class));
            }
        });
    }
}