package com.example.university.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.university.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. فتح صفحة البحث عن الجامعات
        findViewById(R.id.card_search).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        });

        // 2. فتح صفحة المستشار الذكي
        findViewById(R.id.card_ai).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
        });

        // 3. فتح صفحة حساب المعدل
        findViewById(R.id.card_gpa).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GpaActivity.class));
        });

        // 4. فتح صفحة الجدول الدراسي
        findViewById(R.id.card_schedule).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScheduleActivity.class));
        });

        // 5. فتح صفحة أخبار التعليم
        findViewById(R.id.card_news).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScholarshipActivity.class));
        });

        // 6. فتح صفحة المفضلة
        findViewById(R.id.card_favorites).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
        });

        // 7. فتح صفحة الإعدادات
        findViewById(R.id.card_settings).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
    }
}