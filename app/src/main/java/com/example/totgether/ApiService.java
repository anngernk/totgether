package com.example.totgether;

import com.example.totgether.auth.LoginRequest;
import com.example.totgether.auth.LoginResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {
    @POST("api/auth/login")
    Call<ResponseBody> loginUser(@Body LoginRequest loginRequest);
    @POST("api/auth/register")
    Call<ResponseBody> registerUser(@Body LoginRequest loginRequest );

    @GET("patients")
    Call<List<Patient>> getPatientsByDate(@Query("date") String date);
}