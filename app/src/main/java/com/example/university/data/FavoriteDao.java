package com.example.university.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UniversityEntity university);

    @Delete
    void delete(UniversityEntity university);

    @Query("SELECT * FROM favorite_universities ORDER BY name ASC")
    LiveData<List<UniversityEntity>> getAllFavorites();
}