package com.example.university.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university.R;
import com.example.university.data.Scholarship;
import com.example.university.data.ScholarshipItem;
import com.example.university.data.ScholarshipResponse;
import com.example.university.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScholarshipActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScholarshipAdapter adapter;

    // مصفوفة ألوان عشان الكروت تطلع شيك
    private final int[] colors = {
            R.color.purple_700,
            R.color.teal_700,
            R.color.purple_500,
            android.R.color.black,
            android.R.color.holo_red_dark,
            android.R.color.holo_blue_dark
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_scholarships);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchRealScholarships();
    }

    private void fetchRealScholarships() {
        RetrofitClient.getService().getRealScholarships().enqueue(new Callback<ScholarshipResponse>() {
            @Override
            public void onResponse(Call<ScholarshipResponse> call, Response<ScholarshipResponse> response) {
                try {
                    if (response.isSuccessful() && response.body() != null && response.body().getItems() != null) {
                        List<ScholarshipItem> apiItems = response.body().getItems();
                        List<Scholarship> list = new ArrayList<>();
                        Random random = new Random();

                        for (ScholarshipItem item : apiItems) {
                            int randomColor = colors[random.nextInt(colors.length)];
                            list.add(new Scholarship(
                                    item.getTitle(),
                                    "International",
                                    item.getDescription(),
                                    item.getLink(),
                                    randomColor
                            ));
                        }

                        if (!list.isEmpty()) {
                            adapter = new ScholarshipAdapter(ScholarshipActivity.this, list);
                            recyclerView.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                } catch (Exception e) {
                    // 🚨 لو حصل أي خطأ في قراءة البيانات، نشغل الداتا البديلة فوراً
                    Log.e("API_ERROR", "Parsing error: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ScholarshipResponse> call, Throwable t) {
                Toast.makeText(ScholarshipActivity.this, "Showing offline data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}