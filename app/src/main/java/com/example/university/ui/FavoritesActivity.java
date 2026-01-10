package com.example.university.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout; // استيراد LinearLayout
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university.R;
import com.example.university.utils.WebClickListener;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements WebClickListener {

    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private UnivViewModel univViewModel;

    // غيرنا المتغير من TextView لـ LinearLayout عشان يتحكم في الكونتينر كله
    private LinearLayout layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // تعريف الـ Views
        recyclerView = findViewById(R.id.recycler_favorites);

        // هنا بنربط بالـ ID الجديد اللي في الـ XML
        layoutEmpty = findViewById(R.id.layout_empty);

        // تهيئة الـ ViewModel
        univViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(UnivViewModel.class);

        setupRecyclerView();
        observeFavorites();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        // نمرر قائمة فارغة في البداية
        adapter = new FavoritesAdapter(this, new ArrayList<>(), this, univViewModel);
        recyclerView.setAdapter(adapter);
    }

    private void observeFavorites() {
        univViewModel.getAllFavorites().observe(this, favorites -> {
            if (favorites == null || favorites.isEmpty()) {
                // لو القائمة فاضية: اخفي القائمة واظهر الأنيميشن
                recyclerView.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            } else {
                // لو فيه بيانات: اظهر القائمة واخفي الأنيميشن
                recyclerView.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.GONE);
                // تحديث بيانات الـ Adapter
                adapter.updateFavorites(favorites);
            }
        });
    }

    @Override
    public void OnClicked(String web) {
        if (web != null && !web.isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
            startActivity(browserIntent);
        } else {
            Toast.makeText(this, "No web page available", Toast.LENGTH_SHORT).show();
        }
    }
}