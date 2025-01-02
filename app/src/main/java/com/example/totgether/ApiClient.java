package com.example.totgether;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;

import com.example.totgether.auth.AuthInterceptor;

import okhttp3.OkHttpClient;


public class ApiClient {
    private static ApiService apiService;

    public static ApiService getApiService(Context context) {
        if (apiService == null) {
            // Add the interceptor to OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.100.5:8080") // Replace with your base URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
