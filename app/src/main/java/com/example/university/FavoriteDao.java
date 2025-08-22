package com.example.university;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavoriteDao {

    // لإضافة جامعة إلى المفضلة.
    // OnConflictStrategy.REPLACE تعني أنه إذا كانت الجامعة موجودة بالفعل، سيتم استبدالها.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UniversityEntity university);

    // لحذف جامعة من المفضلة.
    @Delete
    void delete(UniversityEntity university);

    // لجلب كل الجامعات المحفوظة في المفضلة، مرتبة أبجديًا بالاسم.
    // لاحظ أننا نستخدم LiveData هنا، وهذا سيجعل البيانات تتحدث تلقائيًا في الواجهة عند أي تغيير.
    @Query("SELECT * FROM favorite_universities ORDER BY name ASC")
    LiveData<List<UniversityEntity>> getAllFavorites();
}