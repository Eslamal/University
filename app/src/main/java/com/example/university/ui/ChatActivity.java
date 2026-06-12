package com.example.university.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.university.R;
import com.example.university.data.Message;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private EditText etMessage;
    private ImageButton btnSend;

    // ⚠️ تأكد إن المفتاح ده بتاعك ومفيش أي مسافات قبله أو بعده
    private static final String API_KEY = "AQ.Ab8RN6JRKkmNMyld71SUTjMZpBvrifwtg2bOWW4KLTGoNmnBKg";

    // 💡 هنستخدم gemini-pro لأنه الأكثر استقراراً ومدعوم في كل الحسابات
// الرابط الأحدث والأسرع (Gemini 1.5 Flash)
// الرابط الصحيح للإصدار الحديث اللي شغال حالياً
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_chat);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);

        adapter = new ChatAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        receiveBotResponse("Hello! I am connected to Gemini AI. Ask me anything about universities!");

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                etMessage.setText("");
                hideKeyboard();
                callGeminiAI(text);
            }
        });
    }

    private void sendMessage(String text) {
        adapter.addMessage(new Message(text, true));
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    private void receiveBotResponse(String text) {
        runOnUiThread(() -> {
            adapter.addMessage(new Message(text, false));
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        });
    }

    private void callGeminiAI(String question) {
        receiveBotResponse("Typing...");

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        String jsonBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + escapeJson(question) + "\" }] }] }";
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    if (adapter != null) adapter.removeLastItem();
                    receiveBotResponse("Failed to connect: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        JSONArray candidates = jsonObject.getJSONArray("candidates");
                        String aiReply = candidates.getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0)
                                .getString("text");

                        runOnUiThread(() -> {
                            if (adapter != null) adapter.removeLastItem();
                            receiveBotResponse(aiReply);
                        });

                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            if (adapter != null) adapter.removeLastItem();
                            receiveBotResponse("Error parsing JSON. Check Logcat.");
                            Log.e("GeminiError", "Parsing error", e);
                        });
                    }
                } else {
                    // 🚨 التعديل السحري هنا: هنقرأ رسالة الخطأ من جوجل ونعرضها
                    String errorDetails = response.body() != null ? response.body().string() : "No details";
                    runOnUiThread(() -> {
                        if (adapter != null) adapter.removeLastItem();
                        receiveBotResponse("Error Code: " + response.code() + "\n\nReason: " + errorDetails);
                        Log.e("GeminiError", errorDetails);
                    });
                }
            }
        });
    }

    private String escapeJson(String data) {
        return data.replace("\"", "\\\"").replace("\n", "\\n");
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}