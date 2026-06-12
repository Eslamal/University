package com.example.university.network;

import com.example.university.data.APIResponse;
import com.example.university.data.ScholarshipResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UniversityApiService {

    @GET("Hipo/university-domains-list/master/world_universities_and_domains.json")
    Call<List<APIResponse>> getAllUniversities();

    @GET
    Call<ScholarshipResponse> getRealScholarships(@retrofit2.http.Url String url);
}