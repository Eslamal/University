package com.example.university.network;

import com.example.university.data.APIResponse;
import com.example.university.data.ScholarshipResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UniversityApiService {
    @GET("search")
    Call<List<APIResponse>> searchUniversities(
            @Query("name") String name,
            @Query("country") String country
    );


    @GET("https://api.rss2json.com/v1/api.json?rss_url=https://scholarship-positions.com/feed/")
    Call<ScholarshipResponse> getRealScholarships();
}