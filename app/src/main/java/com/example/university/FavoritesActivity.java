package com.example.university;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements WebClickListener {

    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private UnivViewModel univViewModel;
    private TextView textViewNoFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // تعريف الـ Views
        recyclerView = findViewById(R.id.recycler_favorites);
        textViewNoFavorites = findViewById(R.id.textView_no_favorites);

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
                recyclerView.setVisibility(View.GONE);
                textViewNoFavorites.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                textViewNoFavorites.setVisibility(View.GONE);
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