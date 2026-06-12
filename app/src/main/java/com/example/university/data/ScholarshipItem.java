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

    public String getTitle() {
        return title != null ? title : "International Scholarship";
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link != null ? link : "https://www.scholars4dev.com/";
    }

    // 💡 الحماية اتعملت هنا
    public String getDescription() {
        if (description == null || description.isEmpty()) {
            return "No details available for this scholarship.";
        }
        return description.replaceAll("<.*?>", "").trim();
    }
}