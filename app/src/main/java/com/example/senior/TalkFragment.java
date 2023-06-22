package com.example.senior;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();

    // RecyclerView와 어댑터 객체 생성
    private RecyclerView recyclerView;
    private Context context;
    private MyAdapter adapter;
    private List<Item> itemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_talk, container, false);
        context = container.getContext();

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();

        itemList.add(new Item(R.drawable.woman1, "선희"));
        itemList.add(new Item(R.drawable.man1, "민준"));
        itemList.add(new Item(R.drawable.man2, "봉진"));
        itemList.add(new Item(R.drawable.man3, "국민"));
        itemList.add(new Item(R.drawable.woman2, "지민"));
        itemList.add(new Item(R.drawable.girl, "서현"));
        itemList.add(new Item(R.drawable.woman3, "숨복"));
        itemList.add(new Item(R.drawable.woman4, "유진"));



        adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);

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
                Log.d("TAG", "onFailure: " + e.getMessage());
                Toast.makeText(context,"Failed to load response due to "+ e.getMessage() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        Toast.makeText(context, "살려줘", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, "Failed to load response due to " + response.body().toString(), Toast.LENGTH_SHORT).show();
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
}