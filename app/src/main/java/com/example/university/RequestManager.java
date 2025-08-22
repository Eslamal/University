package com.example.university;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://universities.hipolabs.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // قمنا بإنشاء الواجهة مرة واحدة هنا
    private final UniversityApiService apiService;

    public RequestManager(Context context) {
        this.context = context;
        // وقمنا بتعريفها في الكونستركتور
        this.apiService = retrofit.create(UniversityApiService.class);
    }

    // هذه هي الدالة الموحدة الجديدة للبحث
    public void searchUniversities(OnFetchDataListener listener, String name, String country) {
        // نتحقق إذا كانت المدخلات فارغة ونحولها إلى null
        // لأن Retrofit تتجاهل الـ Query Parameters التي قيمتها null
        String finalName = (name != null && !name.isEmpty()) ? name : null;
        String finalCountry = (country != null && !country.isEmpty()) ? country : null;

        Call<List<APIResponse>> call = apiService.callUniversity(finalName, finalCountry);

        try {
            call.enqueue(new Callback<List<APIResponse>>() {
                @Override
                public void onResponse(Call<List<APIResponse>> call, Response<List<APIResponse>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Request Failed! Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        listener.onError("Request Failed!"); // إبلاغ الـ listener بالخطأ
                        return;
                    }
                    listener.onResponse(response.body(), response.message());
                }

                @Override
                public void onFailure(Call<List<APIResponse>> call, Throwable t) {
                    listener.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "An Error Occurred!", Toast.LENGTH_SHORT).show();
            listener.onError("An Error Occurred!");
        }
    }

    // هذه هي الواجهة الموحدة
    // تقبل اسم ودولة ويمكن أن يكون أحدهما أو كلاهما null
    public interface UniversityApiService {
        @GET("search")
        Call<List<APIResponse>> callUniversity(
                @Query("name") String name,
                @Query("country") String country
        );
    }
}