package com.example.university;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_universities")
public class UniversityEntity {

    @PrimaryKey
    @NonNull
    private String name;

    private String country;
    private String webPage;

    // يجب إنشاء Constructor و Getters و Setters
    // يمكنك إنشاؤها تلقائيًا في Android Studio بالضغط على Alt + Insert

    public UniversityEntity(@NonNull String name, String country, String webPage) {
        this.name = name;
        this.country = country;
        this.webPage = webPage;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }
}