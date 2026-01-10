package com.example.university.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
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

    // ⚠️⚠️ هام جداً: حط مفتاح الـ API بتاعك هنا مكان الكلمة دي
    private static final String API_KEY = "AIzaSyDYD9hxiVBOoz8LxgwxVBjxxC5Qro8SZEA";

    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recycler_chat);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);

        adapter = new ChatAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // رسالة ترحيب
        receiveBotResponse("Hello! I am connected to Gemini AI. Ask me anything about universities!");

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
                etMessage.setText("");

                // هنا بننادي على الـ AI الحقيقي
                callGeminiAI(text);
            }
        });
    }

    private void sendMessage(String text) {
        adapter.addMessage(new Message(text, true));
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    private void receiveBotResponse(String text) {
        // لازم نستخدم runOnUiThread لأن الرد بيجي من Network Thread
        runOnUiThread(() -> {
            adapter.addMessage(new Message(text, false));
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        });
    }

    private void callGeminiAI(String question) {
        // 1. اعرض رسالة "Typing..." للمستخدم
        receiveBotResponse("Typing...");

        // إعدادات الوقت (Timeout) عشان النت
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
                    // لو حصل خطأ، امسح Typing واعرض المشكلة
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
                            // 2. الرد وصل! امسح كلمة Typing الأول
                            if (adapter != null) adapter.removeLastItem();

                            // 3. اعرض الرد الحقيقي
                            receiveBotResponse(aiReply);
                        });

                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            if (adapter != null) adapter.removeLastItem();
                            receiveBotResponse("Error parsing response.");
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        if (adapter != null) adapter.removeLastItem();
                        receiveBotResponse("Error: " + response.code());
                    });
                }
            }
        });
    }

    // دالة بسيطة عشان لو النص فيه علامات تنصيص ميبوظش الـ JSON
    private String escapeJson(String data) {
        return data.replace("\"", "\\\"").replace("\n", "\\n");
    }
}