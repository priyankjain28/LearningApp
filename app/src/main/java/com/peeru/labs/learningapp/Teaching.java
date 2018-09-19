package com.peeru.labs.learningapp;

import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Teaching extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;
    private Boolean recordFlag = false;
    private int calAmplitude;
    ArrayList<String> matches, words;
    ImageButton playButton;
    TextView textView;
    private int counter = 0;
    private String arrayString[] = {"Hello Ankita,", "Let's begin a new \n challenge today.. ", "Let's start with saying the word"};
    private TextToSpeech tts;
    int count = 0;
    private MediaRecorder mRecorder;
    private MediaPlayer mpintro;
    private int stop = 5;
    private Handler mHandler = new Handler();
    private Runnable mTickExecutor = new Runnable() {
        @Override
        public void run() {
            tick();
            mHandler.postDelayed(mTickExecutor,100);
        }
    };
    private File mOutputFile;
    private long mStartTime = 0;

    private int[] amplitudes = new int[100];
    private int i = 0;
    private  int numberOfWords =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching);
        requestPermission();
        attachUI();
        startIntroScreen();
    }

    private void requestPermission() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},
                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
    }


    private void attachUI() {
        textView = (TextView) findViewById(R.id.textView);
        playButton = (ImageButton) findViewById(R.id.playButton);
        tts = new TextToSpeech(this, this);
        words = (ArrayList<String>) getIntent().getSerializableExtra("WordList");
    }

    private void startIntroScreen() {
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
        count = 0;
        new CountDownTimer(8000, 4000) {
            public void onTick(long millisUntilFinished) {
                if (count == 0) {
                    textView.setText(words.get(numberOfWords));
                    speakOut();
                    count++;
                } else if (count == 1) {
                    count++;
                    textView.setText("Your turn.\nI'm listening..");
                    startRecord();
                }
            }

            public void onFinish() {

            }

        }.start();
    }
    
    
    private void startRecord() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mRecorder.setAudioEncodingBitRate(48000);
        } else {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioEncodingBitRate(64000);
        }
        mRecorder.setAudioSamplingRate(16000);
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartTime = SystemClock.elapsedRealtime();
            mHandler.postDelayed(mTickExecutor, 100);
            Log.d("Voice Recorder","Peeru started recording to "+mOutputFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("Voice Recorder", "Peeru prepare() failed "+e.getMessage());
        }
    }

    protected  void stopRecording(boolean saveFile) {
        textView.setText("Analyzing...");
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        mStartTime = 0;
        mHandler.removeCallbacks(mTickExecutor);
        playButton.setVisibility(View.VISIBLE);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpintro = MediaPlayer.create(getApplicationContext(), Uri.parse(mOutputFile.getAbsolutePath()));
                mpintro.start();
            }
        });

    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/VoiceRecorder/RECORDING_"
                + dateFormat.format(new Date())
                + ".wav");
    }

    private void tick() {
        long time = (mStartTime < 0) ? 0 : (SystemClock.elapsedRealtime() - mStartTime);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
        int milliseconds = (int) (time / 100) % 10;
        //mTimerTextView.setText(minutes+":"+(seconds < 10 ? "0"+seconds : seconds)+"."+milliseconds);

        if (mRecorder != null) {
            amplitudes[i] = mRecorder.getMaxAmplitude();
            calAmplitude = (amplitudes[i] * 100 / 32767);
            //Log.d("Voice Recorder","Peeru amplitude: "+ calAmplitude+ " Time : "+minutes+":"+(seconds < 10 ? "0"+seconds : seconds)+"."+milliseconds);
            //start Speaking
            if(calAmplitude > 10 && !recordFlag){
                recordFlag = true;
            }

            //stop speaking && stop recording
            if(recordFlag && calAmplitude < 5  ){
                recordFlag = false;
                stopRecording(false);
            }
            if (i >= amplitudes.length -1) {
                i = 0;
            } else {
                ++i;
            }
        }
        //Stop after sec
        if(seconds == stop){
            stopRecording(false);
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
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


}
