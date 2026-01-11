package com.example.university.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.university.network.RetrofitClient;
import com.example.university.network.UniversityApiService;
import com.example.university.utils.OnFetchDataListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnivRepository {

    private final FavoriteDao favoriteDao;
    private final ExecutorService executorService;
    private final UniversityApiService apiService;

    public UnivRepository(Application application) {
        UnivDatabase db = UnivDatabase.getDatabase(application);
        this.favoriteDao = db.favoriteDao();
        this.executorService = UnivDatabase.databaseWriteExecutor;
        this.apiService = RetrofitClient.getService();
    }

    public void searchUniversities(OnFetchDataListener listener, String name, String country) {
        String queryName = (name != null && !name.trim().isEmpty()) ? name : null;
        String queryCountry = (country != null && !country.trim().isEmpty()) ? country : null;

        apiService.searchUniversities(queryName, queryCountry).enqueue(new Callback<List<APIResponse>>() {
            @Override
            public void onResponse(Call<List<APIResponse>> call, Response<List<APIResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onResponse(response.body(), response.message());
                } else {
                    listener.onError("Error Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<APIResponse>> call, Throwable t) {
                listener.onError("Connection Failed: " + t.getMessage());
            }
        });
    }

    public LiveData<List<UniversityEntity>> getAllFavorites() {
        return favoriteDao.getAllFavorites();
    }

    public void insertFavorite(UniversityEntity university) {
        executorService.execute(() -> favoriteDao.insert(university));
    }

    public void deleteFavorite(UniversityEntity university) {
        executorService.execute(() -> favoriteDao.delete(university));
    }
}