package com.example.totgether;

import com.google.firebase.firestore.auth.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @GET("api/users/{id}")
    Call<User> getUser(@Path("id") Long id);

    @POST("api/users")
    Call<User> createUser(@Body User user);
}