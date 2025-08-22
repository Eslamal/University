package com.example.university;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class UnivViewModel extends AndroidViewModel {

    private final UnivRepository repository;

    // LiveData لنتائج البحث (كما هي)
    private final MutableLiveData<List<APIResponse>> universityList = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    // *** الإضافة الجديدة ***
    // LiveData لقائمة المفضلة، قادمة مباشرة من قاعدة البيانات
    private final LiveData<List<UniversityEntity>> allFavorites;


    public UnivViewModel(@NonNull Application application) {
        super(application);
        repository = new UnivRepository(application.getApplicationContext());
        // نقوم بتهيئة قائمة المفضلة مرة واحدة هنا
        allFavorites = repository.getAllFavorites();
    }

    // --- دوال خاصة بالبحث ---
    public LiveData<List<APIResponse>> getUniversityList() { return universityList; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void searchUniversities(String name, String country) {
        isLoading.setValue(true);

        OnFetchDataListener listener = new OnFetchDataListener() {
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
                error.postValue(message);
                isLoading.postValue(false);
            }
        };

        repository.searchUniversities(listener, name, country);
    }

    // --- دوال جديدة خاصة بالمفضلة ---

    // دالة لجلب قائمة المفضلة
    public LiveData<List<UniversityEntity>> getAllFavorites() {
        return allFavorites;
    }

    // دالة لإضافة جامعة للمفضلة
    public void insertFavorite(UniversityEntity university) {
        repository.insertFavorite(university);
    }

    // دالة لحذف جامعة من المفضلة
    public void deleteFavorite(UniversityEntity university) {
        repository.deleteFavorite(university);
    }
}