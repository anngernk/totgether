package com.example.totgether.auth;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Retrieve the JWT from SharedPreferences
        String jwt = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                .getString("jwt_token", null);

        Request.Builder requestBuilder = chain.request().newBuilder();

        // Add the JWT to the Authorization header if it exists
        if (jwt != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + jwt);
        }

        Response response = chain.proceed(requestBuilder.build());
        if (response.code() == 401) {
            Log.e("AuthInterceptor", "401 - Unauthorized");
        }

        return response;

   }

}
