package com.example.university.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.university.BuildConfig;
import com.example.university.R;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SummarizerActivity extends AppCompatActivity {

    private EditText etLongText;
    private Button btnSummarize;
    private ProgressBar loader;
    private CardView cardResult;
    private TextView tvSummaryResult;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summarizer);

        etLongText = findViewById(R.id.et_long_text);
        btnSummarize = findViewById(R.id.btn_summarize);
        loader = findViewById(R.id.loader_summarizer);
        cardResult = findViewById(R.id.card_result);
        tvSummaryResult = findViewById(R.id.tv_summary_result);

        client = new OkHttpClient();

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        btnSummarize.setOnClickListener(v -> {
            String textToSummarize = etLongText.getText().toString().trim();
            if (textToSummarize.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_empty_text), Toast.LENGTH_SHORT).show();
                return;
            }
            sendTextToGemini(textToSummarize);
        });
    }

    private void sendTextToGemini(String rawText) {
        loader.setVisibility(View.VISIBLE);
        cardResult.setVisibility(View.GONE);
        btnSummarize.setEnabled(false);

        String prompt = getString(R.string.ai_summarizer_prompt) + rawText;

        JSONObject jsonBody = new JSONObject();
        try {
            JSONArray contentsArray = new JSONArray();
            JSONObject contentObject = new JSONObject();
            JSONArray partsArray = new JSONArray();
            JSONObject partObject = new JSONObject();

            partObject.put("text", prompt);
            partsArray.put(partObject);
            contentObject.put("parts", partsArray);
            contentsArray.put(contentObject);
            jsonBody.put("contents", contentsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=" + BuildConfig.API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    loader.setVisibility(View.GONE);
                    btnSummarize.setEnabled(true);
                    Toast.makeText(SummarizerActivity.this, getString(R.string.msg_connection_failed), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = "";
                if (response.body() != null) {
                    responseString = response.body().string();
                }

                if (response.isSuccessful()) {
                    try {
                        JSONObject root = new JSONObject(responseString);

                        if (root.has("candidates")) {
                            JSONArray candidates = root.getJSONArray("candidates");
                            if (candidates.length() > 0) {
                                JSONObject firstCandidate = candidates.getJSONObject(0);

                                if (firstCandidate.has("content")) {
                                    JSONObject content = firstCandidate.getJSONObject("content");
                                    JSONArray parts = content.getJSONArray("parts");
                                    String aiResult = parts.getJSONObject(0).getString("text");

                                    runOnUiThread(() -> {
                                        loader.setVisibility(View.GONE);
                                        btnSummarize.setEnabled(true);
                                        cardResult.setVisibility(View.VISIBLE);
                                        tvSummaryResult.setText(aiResult);
                                    });
                                } else {
                                    String finishReason = firstCandidate.optString("finishReason", "Unknown");
                                    android.util.Log.e("GeminiError", "Blocked by Google. Reason: " + finishReason);
                                    showCustomError("تم رفض النص من جوجل. السبب: " + finishReason);
                                }
                            }
                        } else {
                            showErrorOnUI();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        android.util.Log.e("GeminiError", "JSON Parse Error. Response: " + responseString);
                        showErrorOnUI();
                    }
                } else {
                    android.util.Log.e("GeminiError", "HTTP Error Code: " + response.code() + " Body: " + responseString);
                    showErrorOnUI();
                }
            }
        });
    }

    private void showCustomError(String msg) {
        runOnUiThread(() -> {
            loader.setVisibility(View.GONE);
            btnSummarize.setEnabled(true);
            Toast.makeText(SummarizerActivity.this, msg, Toast.LENGTH_LONG).show();
        });
    }

    private void showErrorOnUI() {
        runOnUiThread(() -> {
            loader.setVisibility(View.GONE);
            btnSummarize.setEnabled(true);
            Toast.makeText(SummarizerActivity.this, getString(R.string.msg_ai_error), Toast.LENGTH_SHORT).show();
        });
    }
}