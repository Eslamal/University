package com.example.university;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WebClickListener {

    private RecyclerView recyclerView;
    private UnivAdapter adapter;
    private ImageButton button_search; // Renamed for clarity
    private EditText editText_name, editText_country;
    private ProgressBar progressBar;
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

        button_search.setOnClickListener(view -> {
            String name = editText_name.getText().toString().trim();
            String country = editText_country.getText().toString().trim();

            if (name.isEmpty() && country.isEmpty()) {
                Toast.makeText(MainActivity.this, "Enter University name or Country name.", Toast.LENGTH_LONG).show();
                return;
            }
            univViewModel.searchUniversities(name, country);
        });

        // === بداية التعديل ===
        // Find the favorites button and set its click listener
        ImageButton buttonShowFavorites = findViewById(R.id.button_show_favorites);
        buttonShowFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });
        // === نهاية التعديل ===
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.recycler_list);
        editText_name = findViewById(R.id.editText_name);
        editText_country = findViewById(R.id.editText_country);
        button_search = findViewById(R.id.button_search); // Corrected variable name
        progressBar = findViewById(R.id.loader);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    private void observeViewModel() {
        univViewModel.getUniversityList().observe(this, universities -> {
            if (universities != null) {
                showResult(universities);
            }
        });

        univViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        univViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                if (errorMessage.equals("No Data Found!")) {
                    showNoResultsDialog();
                } else {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        univViewModel.getAllFavorites().observe(this, favorites -> {
            Log.d("FAVORITES_DEBUG", "Favorites list updated. Count: " + favorites.size());
            this.favoritesList = favorites;
            if (adapter != null) {
                adapter.setFavorites(favorites);
            }
        });
    }

    private void showResult(List<APIResponse> responses) {
        adapter = new UnivAdapter(this, responses, this, univViewModel);
        adapter.setFavorites(favoritesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoResultsDialog() {
        final Dialog alertDialog = new Dialog(MainActivity.this);
        alertDialog.setContentView(R.layout.custom_alert_dialog);
        alertDialog.setCancelable(true);
        Button ok_button = alertDialog.findViewById(R.id.button_alert);
        ok_button.setOnClickListener(v -> alertDialog.dismiss());
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        alertDialog.show();
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