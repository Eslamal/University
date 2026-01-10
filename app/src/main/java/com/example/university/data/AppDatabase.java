package com.example.university.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Lecture.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract LectureDao lectureDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "lecture_database") // << غيرنا الاسم هنا
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}