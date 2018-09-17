package com.peeru.labs.learningapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Teaching extends AppCompatActivity implements
        TextToSpeech.OnInitListener, RecognitionListener {

    private static final String TAG = "Teaching";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    private Boolean repeat = true;
    private SpeechRecognizer speechRecognizer;
    ArrayList<String> matches,words;
    TextView textView;
    private int counter = 0;
    private int cal=0;
    private String arrayString[] = {"Hello Ankita,", "Let's begin a new \n challenge today.. ", "Let's start with saying the word"};
    private TextToSpeech tts;
    private int numberOfWords;
    int count =0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching);
        textView = (TextView) findViewById(R.id.textView);
        tts = new TextToSpeech(this, this);
         words = (ArrayList<String>)getIntent().getSerializableExtra("WordList");
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        counter = 0;
        new CountDownTimer(9000, 3000) {
            public void onTick(long millisUntilFinished) {
                textView.setText(arrayString[counter++]);
            }

            public void onFinish() {
                loadWordFromPool();
            }

        }.start();

    }

    private void loadWordFromPool() {
        count =0;

        new CountDownTimer(12000, 4000) {
            public void onTick(long millisUntilFinished) {
                if(count == 0) {
                    textView.setText(words.get(numberOfWords));
                    speakOut();
                    count++;
                }else if(count == 1){
                    count++;
                    textView.setText("Your turn.\nI'm listening..");
                    speaking();
                }else{
                    nextWordCall();
                }
            }

            public void onFinish() {
                //loadWordFromPool();
            }

        }.start();

    }

    private void speaking() {
        if (ContextCompat.checkSelfPermission(Teaching.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            startRecognition();

        }
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
    private void startRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1000);
        speechRecognizer.startListening(intent);

    }
    private void showResults(Bundle results) {
        matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Toast.makeText(this, matches.get(0) + " = "+words.get(numberOfWords), Toast.LENGTH_LONG).show();
        sendForAnalyzing(matches.get(0));

    }

    private void sendForAnalyzing(String s) {

        if(s.compareToIgnoreCase(words.get(numberOfWords)) == 0){
            textView.setText("Wow! Sounds Perfect");
            repeat = false;
        }else if(s.equals("")){
            textView.setText("Are you not speaking.");
            repeat = true;
        }else{
            textView.setText("Different word");
            repeat = true;
        }


    }

    private void nextWordCall() {

        if(!repeat){
            numberOfWords++;
            if(numberOfWords > words.size()-1) {
                finish();
            }
        }
        loadWordFromPool();
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this, "Requires RECORD_AUDIO permission", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.RECORD_AUDIO },
                    REQUEST_RECORD_AUDIO_PERMISSION_CODE);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecognition();
                }else{
                    requestPermission();
                }
        }
    }

    private void speakOut() {
        String text = textView.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(new Locale("en"));
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.d(TAG, "Peeru onReadyForSpeech()");
        //onBeginningOfSpeech();
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "Peeru onBeginningOfSpeech()");
        cal=0;
    }

    @Override
    public void onRmsChanged(float v) {
        Log.d(TAG, "Peeru onRmsChanged() "+(cal++));
        if(cal>100){
          sendForAnalyzing("");
        }
    }



    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.d(TAG, "Peeru onBufferReceived()");
    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "Peeru onEndOfSpeech()");
        Log.d(TAG,"Peeru Back");
        sendForAnalyzing("");
       }

    @Override
    public void onError(int error) {
        Log.d(TAG, "onError(): " + error);

        if(error == SpeechRecognizer.ERROR_NO_MATCH)
        {

        }
        else if(error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT)
        {

        }
        else
        {
            textView.setText("Error: " + error);
        }
    }

    @Override
    public void onResults(Bundle bundle) {
        Log.d(TAG, "Peeru onResults()");

        showResults(bundle);
    }

    @Override
    public void onPartialResults(Bundle bundle) {

        Log.d(TAG, "Peeru onPartialResults()");
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.d(TAG, "Peeru onEvent()");
    }
}
