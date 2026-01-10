package com.example.university.data;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ScholarshipResponse {
    @SerializedName("items")
    private List<ScholarshipItem> items;

    public List<ScholarshipItem> getItems() { return items; }
}