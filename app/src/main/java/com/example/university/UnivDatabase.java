package com.example.university;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Database(entities = {UniversityEntity.class}, version = 1, exportSchema = false)
public abstract class UnivDatabase extends RoomDatabase {

    public abstract FavoriteDao favoriteDao();

    private static volatile UnivDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static UnivDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UnivDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UnivDatabase.class, "university_database")
                            // .allowMainThreadQueries() // <<< امسح هذا السطر
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}