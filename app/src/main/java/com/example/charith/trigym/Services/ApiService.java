package com.example.charith.trigym.Services;

import com.google.gson.Gson;

public class ApiService {

    private static final String TAG="TriGym";

    Gson gson = new Gson();

    private static ApiService apiService = new ApiService();

    public ApiService() {

    }

    public static ApiService getInstance() {
        return apiService;
    }


  
}
