package com.example.university.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        TextView tvTitle = findViewById(R.id.header).findViewById(R.id.tv_header_title);

        database = AppDatabase.getDatabase(this);
        recyclerView = findViewById(R.id.recycler_schedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadLectures();

        FloatingActionButton fabAdd = findViewById(R.id.fab_add_lecture);
        // عند الضغط على الزر العائم -> إضافة محاضرة جديدة (نرسل null)
        fabAdd.setOnClickListener(v -> showLectureDialog(null));
    }

    private void loadLectures() {
        lectureList = database.lectureDao().getAllLectures();
        if (adapter == null) {
            // نمرر this::showLectureDialog عشان لما نضغط على عنصر يفتح التعديل
            adapter = new LectureAdapter(lectureList, this::showLectureDialog);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(lectureList);
        }
    }

    // دالة واحدة للإضافة والتعديل
    private void showLectureDialog(Lecture lectureToEdit) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_lecture);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // ربط العناصر
        TextView tvTitle = dialog.findViewById(R.id.tv_dialog_title);
        EditText etCourse = dialog.findViewById(R.id.et_course_name);
        EditText etRoom = dialog.findViewById(R.id.et_room);
        EditText etProfessor = dialog.findViewById(R.id.et_professor);
        EditText etDay = dialog.findViewById(R.id.et_day);
        EditText etTime = dialog.findViewById(R.id.et_time);
        Button btnSave = dialog.findViewById(R.id.btn_save_lecture);
        Button btnDelete = dialog.findViewById(R.id.btn_delete_lecture); // زرار الحذف الجديد

        // تحديد الوضع (إضافة أم تعديل)
        if (lectureToEdit == null) {
            // وضع الإضافة
            tvTitle.setText(getString(R.string.dialog_add_title));
            btnSave.setText(getString(R.string.btn_save));
            btnDelete.setVisibility(View.GONE); // إخفاء زر الحذف
        } else {
            // وضع التعديل (ملء البيانات القديمة)
            tvTitle.setText(getString(R.string.dialog_edit_title));
            btnSave.setText(getString(R.string.btn_update));
            btnDelete.setVisibility(View.VISIBLE); // إظهار زر الحذف

            etCourse.setText(lectureToEdit.courseName);
            etRoom.setText(lectureToEdit.room);
            etProfessor.setText(lectureToEdit.professor);
            etDay.setText(lectureToEdit.day);
            etTime.setText(lectureToEdit.time);
        }

        // برمجة زر الحفظ/التعديل
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
                // إنشاء جديد
                Lecture newLecture = new Lecture(course, room, professor, day, time, getRandomColor());
                database.lectureDao().insert(newLecture);
                Toast.makeText(this, getString(R.string.msg_added), Toast.LENGTH_SHORT).show();
            } else {
                // تعديل الحالي (تحديث البيانات في نفس الكائن)
                lectureToEdit.courseName = course;
                lectureToEdit.room = room;
                lectureToEdit.professor = professor;
                lectureToEdit.day = day;
                lectureToEdit.time = time;
                // الـ ID واللون زي ما هما
                database.lectureDao().update(lectureToEdit);
                Toast.makeText(this, getString(R.string.msg_updated), Toast.LENGTH_SHORT).show();
            }

            loadLectures();
            dialog.dismiss();
        });

        // برمجة زر الحذف
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