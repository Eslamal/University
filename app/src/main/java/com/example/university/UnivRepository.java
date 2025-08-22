package com.example.university;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;

// Repository هو الوسيط بين مصدر البيانات والـ ViewModel
public class UnivRepository {

    private final RequestManager requestManager;
    private final FavoriteDao favoriteDao;
    private final ExecutorService databaseWriteExecutor;

    public UnivRepository(Context context) {
        this.requestManager = new RequestManager(context);
        UnivDatabase database = UnivDatabase.getDatabase(context);
        this.favoriteDao = database.favoriteDao();
        this.databaseWriteExecutor = UnivDatabase.databaseWriteExecutor;
    }

    // --- دوال خاصة بالبحث من الإنترنت (كما هي) ---
    public void searchUniversities(OnFetchDataListener listener, String name, String country) {
        requestManager.searchUniversities(listener, name, country);
    }

    // --- دوال جديدة خاصة بقاعدة البيانات المحلية (المفضلة) ---

    // دالة لجلب كل الجامعات المفضلة (تُرجع LiveData)
    public LiveData<List<UniversityEntity>> getAllFavorites() {
        return favoriteDao.getAllFavorites();
    }

    // دالة لإضافة جامعة للمفضلة (تنفذ في الخلفية)
    public void insertFavorite(UniversityEntity university) {
        databaseWriteExecutor.execute(() -> {
            favoriteDao.insert(university);
        });
    }

    // دالة لحذف جامعة من المفضلة (تنفذ في الخلفية)
    public void deleteFavorite(UniversityEntity university) {
        databaseWriteExecutor.execute(() -> {
            favoriteDao.delete(university);
        });
    }
}