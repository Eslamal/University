package com.example.university.data;

import com.google.gson.annotations.SerializedName;

public class ScholarshipItem {
    @SerializedName("title")
    private String title;

    @SerializedName("pubDate")
    private String pubDate;

    @SerializedName("link")
    private String link;

    @SerializedName("description")
    private String description;

    public String getTitle() { return title; }
    public String getPubDate() { return pubDate; }
    public String getLink() { return link; }

    public String getDescription() {
        return description.replaceAll("<.*?>", "").trim();
    }
}