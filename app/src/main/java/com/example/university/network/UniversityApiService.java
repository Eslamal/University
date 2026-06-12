package com.example.university.network;

import com.example.university.data.APIResponse;
import com.example.university.data.ScholarshipResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UniversityApiService {

    // سحب الجامعات
    @GET("Hipo/university-domains-list/master/world_universities_and_domains.json")
    Call<List<APIResponse>> getAllUniversities();

    // 💡 رابط BBC للأخبار التعليمية (مفتوح ومش بيعمل بلوك) عشان نختبر الـ API
    @GET("https://api.rss2json.com/v1/api.json?rss_url=http://feeds.bbci.co.uk/news/education/rss.xml")
    Call<ScholarshipResponse> getRealScholarships();
}