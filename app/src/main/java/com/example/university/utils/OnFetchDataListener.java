package com.example.university.utils;

import com.example.university.data.APIResponse;

import java.util.List;

public interface OnFetchDataListener {
    void onResponse(List<APIResponse> responses, String message);
    void onError(String message);
}
