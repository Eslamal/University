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
    public int color; // لون الشريط الجانبي

    // Constructor
    public Lecture(String courseName, String room, String professor, String day, String time, int color) {
        this.courseName = courseName;
        this.room = room;
        this.professor = professor;
        this.day = day;
        this.time = time;
        this.color = color;
    }
}