package com.example.university.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.university.R;
import com.example.university.data.AppDatabase;
import com.example.university.data.Lecture;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LectureAdapter adapter;
    private AppDatabase database;
    private List<Lecture> lectureList = new ArrayList<>();

    // تعريف المتغير بتاع الشاشة الفاضية
    private LinearLayout layoutEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        database = AppDatabase.getDatabase(this);
        recyclerView = findViewById(R.id.recycler_schedule);
        layoutEmptyState = findViewById(R.id.layout_empty_state); // ربط التصميم

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadLectures();

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_lecture);
        fabAdd.setOnClickListener(v -> showLectureDialog(null));
    }

    private void loadLectures() {
        lectureList = database.lectureDao().getAllLectures();

        // 💡 فحص: هل الجدول فاضي؟
        if (lectureList.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (adapter == null) {
            adapter = new LectureAdapter(lectureList, this::showLectureDialog);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(lectureList);
        }
    }

    private void showLectureDialog(Lecture lectureToEdit) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_lecture);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvTitle = dialog.findViewById(R.id.tv_dialog_title);
        EditText etCourse = dialog.findViewById(R.id.et_course_name);
        EditText etRoom = dialog.findViewById(R.id.et_room);
        EditText etProfessor = dialog.findViewById(R.id.et_professor);
        EditText etDay = dialog.findViewById(R.id.et_day);
        EditText etTime = dialog.findViewById(R.id.et_time);
        Button btnSave = dialog.findViewById(R.id.btn_save_lecture);
        Button btnDelete = dialog.findViewById(R.id.btn_delete_lecture);

        if (lectureToEdit == null) {
            tvTitle.setText(getString(R.string.dialog_add_title));
            btnSave.setText(getString(R.string.btn_save));
            btnDelete.setVisibility(View.GONE);
        } else {
            tvTitle.setText(getString(R.string.dialog_edit_title));
            btnSave.setText(getString(R.string.btn_update));
            btnDelete.setVisibility(View.VISIBLE);

            etCourse.setText(lectureToEdit.courseName);
            etRoom.setText(lectureToEdit.room);
            etProfessor.setText(lectureToEdit.professor);
            etDay.setText(lectureToEdit.day);
            etTime.setText(lectureToEdit.time);
        }

        btnSave.setOnClickListener(v -> {
            String course = etCourse.getText().toString().trim();
            String room = etRoom.getText().toString().trim();
            String professor = etProfessor.getText().toString().trim();
            String day = etDay.getText().toString().trim();
            String time = etTime.getText().toString().trim();

            if (course.isEmpty() || day.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_fill_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            if (lectureToEdit == null) {
                Lecture newLecture = new Lecture(course, room, professor, day, time, getRandomColor());
                database.lectureDao().insert(newLecture);
                Toast.makeText(this, getString(R.string.msg_added), Toast.LENGTH_SHORT).show();
            } else {
                lectureToEdit.courseName = course;
                lectureToEdit.room = room;
                lectureToEdit.professor = professor;
                lectureToEdit.day = day;
                lectureToEdit.time = time;
                database.lectureDao().update(lectureToEdit);
                Toast.makeText(this, getString(R.string.msg_updated), Toast.LENGTH_SHORT).show();
            }

            loadLectures();
            dialog.dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            if (lectureToEdit != null) {
                database.lectureDao().delete(lectureToEdit);
                Toast.makeText(this, getString(R.string.msg_deleted), Toast.LENGTH_SHORT).show();
                loadLectures();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private int getRandomColor() {
        int[] colors = {
                Color.parseColor("#E91E63"), Color.parseColor("#9C27B0"),
                Color.parseColor("#2196F3"), Color.parseColor("#009688"),
                Color.parseColor("#FF9800"), Color.parseColor("#F44336")
        };
        return colors[new Random().nextInt(colors.length)];
    }
}