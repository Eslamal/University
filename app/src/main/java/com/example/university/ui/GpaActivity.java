package com.example.university.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.university.R;
import java.util.ArrayList;
import java.util.List;

public class GpaActivity extends AppCompatActivity {

    private LinearLayout layoutCourseList;
    private TextView tvResult;
    private final String[] grades = {"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F"};
    private final double[] gradePoints = {4.0, 4.0, 3.7, 3.3, 3.0, 2.7, 2.3, 2.0, 1.7, 1.3, 1.0, 0.0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);

        layoutCourseList = findViewById(R.id.layout_course_list);
        tvResult = findViewById(R.id.tv_gpa_result);
        Button btnAdd = findViewById(R.id.btn_add_course);
        Button btnCalc = findViewById(R.id.btn_calculate);

        addCourseRow();

        btnAdd.setOnClickListener(v -> addCourseRow());

        btnCalc.setOnClickListener(v -> calculateGPA());
    }

    private void addCourseRow() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_course_row, layoutCourseList, false);

        Spinner spinnerGrade = view.findViewById(R.id.spinner_grade);
        ImageButton btnRemove = view.findViewById(R.id.btn_remove);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(adapter);

        btnRemove.setOnClickListener(v -> {
            layoutCourseList.removeView(view);
        });

        layoutCourseList.addView(view);
    }

    private void calculateGPA() {
        double totalPoints = 0.0;
        double totalCredits = 0.0;

        for (int i = 0; i < layoutCourseList.getChildCount(); i++) {
            View view = layoutCourseList.getChildAt(i);

            EditText etCredits = view.findViewById(R.id.et_credits);
            Spinner spinnerGrade = view.findViewById(R.id.spinner_grade);

            String creditStr = etCredits.getText().toString();
            if (!creditStr.isEmpty()) {
                double credit = Double.parseDouble(creditStr);
                int gradeIndex = spinnerGrade.getSelectedItemPosition();
                double point = gradePoints[gradeIndex];

                totalPoints += (point * credit);
                totalCredits += credit;
            }
        }

        if (totalCredits > 0) {
            double gpa = totalPoints / totalCredits;
            tvResult.setText(String.format("%.2f", gpa));
        } else {
            tvResult.setText("0.00");
            Toast.makeText(this, getString(R.string.error_enter_credits), Toast.LENGTH_SHORT).show();
        }
    }
}