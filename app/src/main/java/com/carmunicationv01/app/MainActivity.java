package com.carmunicationv01.app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {
    private Button inputMic;
    private ScrollView displayInputText;
    private TextView userInputHolder;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    public String userMessage = "";

    public boolean checkForDisablingHandsFree(String userMessage){
        boolean stopHandFree = false;
        String[] allWords = userMessage.split(" ");
        String stopSentence = " stop hands free";
        String stopSentenceV2 = " stop hands-free";
        String sentenceFormedSoFar = "";

        for ( String currentWord : allWords) {
            Log. d("See", "Speak  " + currentWord);
            if (currentWord.equals("stop")  || currentWord.equals("hands") || currentWord.equals("free")
                    || currentWord.equals("hands-free")  ){
                sentenceFormedSoFar = sentenceFormedSoFar + " " + currentWord;
            }
            if (stopSentence.equals(sentenceFormedSoFar) || stopSentenceV2.equals(sentenceFormedSoFar)){
                stopHandFree = true;
                Log. d("Seee", "works");
                break;
            }
        }
        Log. d("SeeMessages", "Speak to text:" + sentenceFormedSoFar + ":");

        return stopHandFree;
    }

    public void displayUserWords(Intent data){
        userInputHolder = findViewById(R.id.messageHistory);
        ArrayList<String> result2 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        userInputHolder.setText(Objects.requireNonNull(result2).get(0));
        userMessage = TextUtils.join(", ", result2);
        Log. d("SeeMessage", "Testtttttttttttttt " + result2);


        // Repeat process till stopped:

        if(checkForDisablingHandsFree(userMessage) == false) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");


            someActivityResultLauncher.launch(intent);
        }

    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
    new ActivityResultCallback<ActivityResult>() {


        @Override
        public void onActivityResult(ActivityResult result) {
            Log. d("Ta", "hhhhhhhhhhhhhh" + result);
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Intent data = result.getData();
                Log. d("Tagged", "Tester hesssss");
                displayUserWords(data);
                //: hhhhhhhhhhhhhhActivityResult{resultCode=RESULT_CANCELED, data=null}
            }
        }

    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMic = findViewById(R.id.activateHandsFree_Button);
        displayInputText = findViewById(R.id.bluetoothDevice_ScrollTxt);

        inputMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");


                someActivityResultLauncher.launch(intent);

            }
        });





        //Code to inform if bluetooth is off/ unconnected
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("This is an alert with no consequence");
        dlgAlert.setTitle("Carmunication");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        // links xml with the code
        Button bluetoothConnButton = findViewById(R.id.bluthoothConnection_Button);
        bluetoothConnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // use below to send data between pages
                Intent intent = new Intent(MainActivity.this, BluetoothConnections.class);
                intent.putExtra("key","harry");
                // use below move between pages
                startActivity(new Intent(MainActivity.this, BluetoothConnections.class));
            }
        });

        // links xml with the code
        Button helpPageLoad = findViewById(R.id.help_Button);
        helpPageLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // use below move between pages
                startActivity(new Intent(MainActivity.this, help_Page.class));
            }
        });

        // exit app
        Button exit = findViewById(R.id.exit_Button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}