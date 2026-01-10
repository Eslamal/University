package com.example.university.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update; // ⚠️ تأكد من إضافة هذا السطر
import java.util.List;

@Dao
public interface LectureDao {

    @Insert
    void insert(Lecture lecture);

    @Delete
    void delete(Lecture lecture);

    // دالة التعديل الجديدة
    @Update
    void update(Lecture lecture);

    @Query("SELECT * FROM lecture_table")
    List<Lecture> getAllLectures();
}