package com.example.senior;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // give permission to use microphone
        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
        }

        Button btn = findViewById(R.id.temp_btn);
        btn.setOnClickListener(v -> {
            startRecognition();
        });


    }


    // start recognition
    public void startRecognition() {
        // speech config listener
        SpeechConfig speechConfig = SpeechConfig.fromSubscription("84e519c9a7a243c9926aae596b677979", "koreacentral");
        speechConfig.setSpeechRecognitionLanguage("ko-KR");
        AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
        SpeechRecognizer reco = new SpeechRecognizer(speechConfig, audioConfig);

        // start listening
        Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();
        try {
            SpeechRecognitionResult result = task.get();
            Log.i("SpeechSDKDemo", result.getText());
            Log.d("SpeechSDKDemo", result.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // close the reco
        reco.close();
    }
}