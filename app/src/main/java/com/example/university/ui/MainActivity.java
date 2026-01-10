package com.example.university.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.university.R;
import com.example.university.utils.WebClickListener;
import com.example.university.data.UniversityEntity;
import com.example.university.data.APIResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WebClickListener {

    private RecyclerView recyclerView;
    private UnivAdapter adapter;
    private ImageButton button_search, btnSettings;
    private EditText editText_name, editText_country;
    private ProgressBar progressBar;
    private LottieAnimationView animationView; // تعريف Lottie
    private UnivViewModel univViewModel;
    private List<UniversityEntity> favoritesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        univViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UnivViewModel.class);

        setupViews();
        setupRecyclerView();
        observeViewModel();
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.recycler_list);
        editText_name = findViewById(R.id.editText_name);
        editText_country = findViewById(R.id.editText_country);
        button_search = findViewById(R.id.button_search);
        progressBar = findViewById(R.id.loader);

        // ربط العناصر الجديدة
        animationView = findViewById(R.id.animation_view);
        btnSettings = findViewById(R.id.btn_settings);

        // برمجة زرار البحث
        button_search.setOnClickListener(view -> {
            String name = editText_name.getText().toString().trim();
            String country = editText_country.getText().toString().trim();

            if (name.isEmpty() && country.isEmpty()) {
                Toast.makeText(MainActivity.this, getString(R.string.no_data), Toast.LENGTH_LONG).show();
                return;
            }
            univViewModel.searchUniversities(name, country);
        });

        // برمجة زرار المفضلة
        ImageButton buttonShowFavorites = findViewById(R.id.button_show_favorites);
        buttonShowFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // جوه دالة setupViews
        ImageButton btnScholarships = findViewById(R.id.btn_scholarships);

        btnScholarships.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScholarshipActivity.class);
            startActivity(intent);
        });

        // تعريف الزرار
// (تأكد إنك ضفت زرار في activity_main.xml وسميته btn_schedule مثلاً)
        ImageButton btnSchedule = findViewById(R.id.btn_schedule);

        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });


        ImageButton btnAiChat = findViewById(R.id.btn_ai_chat);
        btnAiChat.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
        });

        // برمجة زرار الإعدادات (Settings)
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
        // ضيف زرار جديد في الـ Layout وسميه btn_gpa
        ImageButton btnGpa = findViewById(R.id.btn_gpa); // افترضنا انك ضفته

        btnGpa.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GpaActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void observeViewModel() {
        // مراقبة البيانات
        univViewModel.getUniversityList().observe(this, universities -> {
            if (universities != null && !universities.isEmpty()) {
                showResult(universities);
            } else {
                showEmptyState();
            }
        });

        // مراقبة التحميل
        univViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                animationView.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        univViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                // هذا السطر سيطبع الخطأ في الـ Logcat بالأسفل
                Log.e("API_ERROR", "سبب المشكلة: " + errorMessage);

                // إظهار رسالة توست للمستخدم مؤقتاً لنعرف السبب
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

                showEmptyState();
            }
        });

        // مراقبة المفضلة لتحديث القلوب في الليستة
        univViewModel.getAllFavorites().observe(this, favorites -> {
            Log.d("FAVORITES_DEBUG", "Favorites list updated. Count: " + favorites.size());
            this.favoritesList = favorites;
            if (adapter != null) {
                adapter.setFavorites(favorites);
            }
        });
    }

    private void showResult(List<APIResponse> responses) {
        // إخفاء الأنيميشن وإظهار الليستة
        animationView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        adapter = new UnivAdapter(this, responses, this, univViewModel);
        adapter.setFavorites(favoritesList);
        recyclerView.setAdapter(adapter);
    }

    private void showEmptyState() {
        // إخفاء الليستة وإظهار الأنيميشن
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
    }

    @Override
    public void OnClicked(String web) {
        if (web != null && !web.isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
            startActivity(browserIntent);
        } else {
            Toast.makeText(this, "Invalid web page URL", Toast.LENGTH_SHORT).show();
        }
    }
}