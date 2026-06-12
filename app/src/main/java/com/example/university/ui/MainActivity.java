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

        findViewById(R.id.card_search).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        });

        findViewById(R.id.card_ai).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
        });

        findViewById(R.id.card_gpa).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GpaActivity.class));
        });

        findViewById(R.id.card_schedule).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScheduleActivity.class));
        });

        findViewById(R.id.card_news).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ScholarshipActivity.class));
        });

        findViewById(R.id.card_favorites).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
        });

        findViewById(R.id.card_settings).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
    }
}