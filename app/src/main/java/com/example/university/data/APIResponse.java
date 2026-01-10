package com.example.university.data;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class APIResponse {

    public APIResponse(String name, String country, String alphaTwoCode, String webPage) {
        this.name = name;
        this.country = country;
        this.alphaTwoCode = alphaTwoCode;
        this.webPages = new java.util.ArrayList<>();
        this.webPages.add(webPage);
    }

    @SerializedName("alpha_two_code")
    private String alphaTwoCode = "";

    @SerializedName("web_pages")
    private List<String> webPages = null;

    @SerializedName("name")
    private String name = "";

    @SerializedName("country")
    private String country = "";

    @SerializedName("domains")
    private List<String> domains = null;

    @SerializedName("state-province")
    private String stateProvince = "";

    // Getter methods
    public String getAlphaTwoCode() {
        return alphaTwoCode;
    }

    public List<String> getWebPages() {
        return webPages;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public List<String> getDomains() {
        return domains;
    }

    public String getStateProvince() {
        return stateProvince;
    }
}
