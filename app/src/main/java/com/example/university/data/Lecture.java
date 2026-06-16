package com.example.university.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lecture_table")
public class Lecture {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String courseName;
    public String room;
    public String professor;
    public String day;
    public String time;
    public int color;

    // المتغير الجديد لتتبع عدد مرات الغياب
    private int absenceCount = 0;

    // الـ Constructor الأساسي (بدون الغياب عشان يفضل متوافق مع الكود القديم عند الإضافة)
    public Lecture(String courseName, String room, String professor, String day, String time, int color) {
        this.courseName = courseName;
        this.room = room;
        this.professor = professor;
        this.day = day;
        this.time = time;
        this.color = color;
    }

    // --- Getters & Setters ---

    // دالة لجلب عدد مرات الغياب (عشان الـ Room والـ Adapter يقرأوه)
    public int getAbsenceCount() {
        return absenceCount;
    }

    // دالة لتعديل عدد مرات الغياب (لما المستخدم يضغط على الأزرار)
    public void setAbsenceCount(int absenceCount) {
        this.absenceCount = absenceCount;
    }
}