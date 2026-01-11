package com.example.university.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.university.R;
import com.example.university.data.Scholarship;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship);

        RecyclerView recyclerView = findViewById(R.id.recycler_scholarships);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        List<Scholarship> list = new ArrayList<>();

        list.add(new Scholarship("Chevening", "United Kingdom",
                "Fully funded master's degrees for international students in the UK.",
                "https://www.chevening.org/", R.color.purple_700));

        list.add(new Scholarship("Fulbright", "USA",
                "The flagship international educational exchange program sponsored by the U.S. government.",
                "https://fulbrightonline.org/", R.color.teal_700));

        list.add(new Scholarship("DAAD", "Germany",
                "German Academic Exchange Service offers grants for students and researchers.",
                "https://www.daad.de/en/", R.color.black));

        list.add(new Scholarship("Erasmus+", "Europe",
                "EU programme for education, training, youth and sport in Europe.",
                "https://erasmus-plus.ec.europa.eu/", R.color.purple_500));

        list.add(new Scholarship("Turkiye Burslari", "Turkey",
                "Turkey Scholarships is a government-funded, competitive scholarship program.",
                "https://www.turkiyeburslari.gov.tr/", R.color.red_dark));

        ScholarshipAdapter adapter = new ScholarshipAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }
}