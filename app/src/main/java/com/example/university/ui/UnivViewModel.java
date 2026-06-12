package com.example.university.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.university.data.APIResponse;
import com.example.university.data.UnivRepository;
import com.example.university.data.UniversityEntity;
import com.example.university.utils.OnFetchDataListener;
import java.util.List;

public class UnivViewModel extends AndroidViewModel {

    private final UnivRepository repository;

    private final MutableLiveData<List<APIResponse>> universityList = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final LiveData<List<UniversityEntity>> allFavorites;

    public UnivViewModel(@NonNull Application application) {
        super(application);
        repository = new UnivRepository(application);
        allFavorites = repository.getAllFavorites();
    }

    public LiveData<List<APIResponse>> getUniversityList() { return universityList; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void searchUniversities(String name, String country) {
        isLoading.setValue(true);
        String queryName = (name != null && !name.trim().isEmpty()) ? name : null;
        String queryCountry = (country != null && !country.trim().isEmpty()) ? country : null;

        repository.searchUniversities(new OnFetchDataListener() {
            @Override
            public void onResponse(List<APIResponse> responses, String message) {
                if (responses == null || responses.isEmpty()) {
                    error.postValue("No Data Found!");
                } else {
                    universityList.postValue(responses);
                }
                isLoading.postValue(false);
            }

            @Override
            public void onError(String message) {
                // تم إزالة البيانات الوهمية (Fake Data)
                // الآن سيعرض رسالة خطأ حقيقية إذا فشل الاتصال بالإنترنت
                error.postValue("Error: " + message);
                isLoading.postValue(false);
            }
        }, queryName, queryCountry);
    }

    public LiveData<List<UniversityEntity>> getAllFavorites() {
        return allFavorites;
    }

    public void insertFavorite(UniversityEntity university) {
        repository.insertFavorite(university);
    }

    public void deleteFavorite(UniversityEntity university) {
        repository.deleteFavorite(university);
    }
}