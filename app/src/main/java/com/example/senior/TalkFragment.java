package com.example.senior;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TalkFragment extends Fragment {
    private View view;
    private Context context;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_talk, container, false);
        context = container.getContext();

        // give permission to use microphone
        if (getActivity().checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            getActivity().requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);

        view.findViewById(R.id.temp_btn).setOnClickListener(view1 -> {
            startRecognition();
        });

        return view;
    }

    void callAPI(String question){

        //okhttp
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt",question);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0);
        }catch(JSONException e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(body)
                .build();



        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("callAPI", "Failed to load response due to "+ e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        startSynthesis(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Log.d("callAPI", "Failed to load response due to " + response.body().string());
                }
            }
        });
    }



    // start recognition
    public void startRecognition() {
        String question = "";

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
            question = result.getText();
        } catch (InterruptedException e) {
            e.printStackTrace();
            question =null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            question =null;
        }

        // close the reco
        reco.close();
        callAPI(question);
    }

    // tts
    public void startSynthesis(String resultText) throws ExecutionException, InterruptedException {
        String subscriptionRegion = "koreacentral";

        SpeechConfig config = SpeechConfig.fromSubscription("84e519c9a7a243c9926aae596b677979", subscriptionRegion);
        config.setSpeechSynthesisVoiceName("ko-KR-GookMinNeural");

        SpeechSynthesizer synthesizer = new SpeechSynthesizer(config);
        {
            SpeechSynthesisResult result = synthesizer.SpeakTextAsync(resultText).get();
            if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                Log.d("startSynthesis", "Speech synthesized to speaker for text [" + resultText + "]");
            }
            else if (result.getReason() == ResultReason.Canceled) {
                SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(result);
                Log.d("startSynthesis", "CANCELED: Reason=" + cancellation.getReason());

                if (cancellation.getReason() == CancellationReason.Error) {
                    Log.d("startSynthesis", "CANCELED: ErrorCode=" + cancellation.getErrorCode());
                    Log.d("startSynthesis", "CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
                    Log.d("startSynthesis", "CANCELED: Did you update the subscription info?");
                }
            }
            result.close();
        }
        synthesizer.close();

    }
}