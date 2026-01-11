package com.example.university.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

    private LinearLayout layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recycler_favorites);

        layoutEmpty = findViewById(R.id.layout_empty);

        univViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(UnivViewModel.class);

        setupRecyclerView();
        observeFavorites();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new FavoritesAdapter(this, new ArrayList<>(), this, univViewModel);
        recyclerView.setAdapter(adapter);
    }

    private void observeFavorites() {
        univViewModel.getAllFavorites().observe(this, favorites -> {
            if (favorites == null || favorites.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.GONE);
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