package com.example.university.data;

public class Scholarship {
    private String name;
    private String country;
    private String description;
    private String link;
    private int colorRes; // لون مميز لكل منحة

    public Scholarship(String name, String country, String description, String link, int colorRes) {
        this.name = name;
        this.country = country;
        this.description = description;
        this.link = link;
        this.colorRes = colorRes;
    }

    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getDescription() { return description; }
    public String getLink() { return link; }
    public int getColorRes() { return colorRes; }
}