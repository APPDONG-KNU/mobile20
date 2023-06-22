package com.example.senior;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator3;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TalkFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private View view;
    private Context context;

    private int currentPosition = 0;
    private int direction = 1;
    private Handler handler;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
    // RecyclerView와 어댑터 객체 생성
    private ViewPager2 viewPager;
    private MyAdapter adapter;
    private CircleIndicator3 indicator;
    private List<Item> itemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_talk, container, false);
        context = container.getContext();

        // give permission to use microphone
        if (getActivity().checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            getActivity().requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
        viewPager = view.findViewById(R.id.viewPager);
        indicator = view.findViewById(R.id.indicator);


        itemList.add(new Item(R.drawable.woman1, "김선희(여)","저는 대한민국 대구광역시에\n사는 32세 여성입니다"));
        itemList.add(new Item(R.drawable.man1, "최민준(남)","저는 대한민국 부산광역시에\n사는 31세 남성입니다"));
        itemList.add(new Item(R.drawable.man2, "정봉진(남)","저는 대한민국 세종특별자치시에\n사는 43세 남성입니다"));
        itemList.add(new Item(R.drawable.man3, "조국민(남)","저는 대한민국 대전광역시에 사는\n27세 남성입니다"));
        itemList.add(new Item(R.drawable.woman2, "신지민(여)","저는 대한민국 서울특별시에 사는\n51세 여성입니다"));
        itemList.add(new Item(R.drawable.girl, "이서현(여)","저는 대한민국 대구광역시에 사는\n8세 여자아이입니다"));
        itemList.add(new Item(R.drawable.woman3, "최숨복(여)","저는 대한민국 광주광역시에 사는\n82세 여성입니다"));
        itemList.add(new Item(R.drawable.woman4, "안유진(여)","저는 대한민국 제주도에 사는\n19세 여성입니다"));

        adapter = new MyAdapter(itemList);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }
        });
        view.findViewById(R.id.temp_btn).setOnClickListener(view1 -> {
            startRecognition();
        });


        return view;
    }

    // viewpager callback methods

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int Position) {
        currentPosition = Position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
                .header("Authorization","Bearer " + BuildConfig.OPENAI_KEY)
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
                        startSynthesis(result.trim(), currentPosition);
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
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(BuildConfig.SPEECH_KEY, "koreacentral");
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
        callAPI(question + "나는 노인이야");
    }

    // tts
    public void startSynthesis(String resultText, int position) throws ExecutionException, InterruptedException {
        String subscriptionRegion = "koreacentral";
        SpeechConfig config = SpeechConfig.fromSubscription(BuildConfig.SPEECH_KEY, subscriptionRegion);
        config.setSpeechSynthesisVoiceName("ko-KR-SunHiNeural");
        Log.i("position", String.valueOf(position));
        switch(position){

            case 0:
                config.setSpeechSynthesisVoiceName("ko-KR-SunHiNeural");
                break;
            case 1:
                config.setSpeechSynthesisVoiceName("ko-KR-InJoonNeural");
                break;
            case 2:
                config.setSpeechSynthesisVoiceName("ko-KR-BongJinNeural");
                break;
            case 3:
                config.setSpeechSynthesisVoiceName("ko-KR-GookMinNeural");
                break;
            case 4:
                config.setSpeechSynthesisVoiceName("ko-KR-JiMinNeural");
                break;
            case 5:
                config.setSpeechSynthesisVoiceName("ko-KR-SeoHyeonNeural");
                break;
            case 6:
                config.setSpeechSynthesisVoiceName("ko-KR-SoonBokNeural");
                break;
            case 7:
                config.setSpeechSynthesisVoiceName("ko-KR-YuJinNeural");
                break;
        }

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