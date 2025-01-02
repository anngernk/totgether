package com.example.totgether.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.totgether.ApiClient;
import com.example.totgether.ApiService;
import com.example.totgether.MainActivity; // Adjust this to your actual post-login activity
import com.example.totgether.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String login = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(login, password);
        });
    }

    private void loginUser(String login, String password) {
        ApiService apiService = ApiClient.getApiService(LoginActivity.this);
        Log.i("LoginActivity", "Login endpoint called");
        Call<ResponseBody> call = apiService.loginUser(new LoginRequest(login, password));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                handleServerResponse(response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("LoginActivity", "Request failed: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleServerResponse(Response<ResponseBody> response) {
        if (response.isSuccessful() && response.body() != null) {
            try {
                String responseBody = response.body().string();
                Log.i("LoginActivity", "Response: " + responseBody);

                // Extract JWT token from the response body
                String jwt = extractJwtFromResponse(responseBody);
                if (jwt != null) {
                    saveJwt(jwt);
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to the main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Token not found in response", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.e("LoginActivity", "Error reading response body", e);
                Toast.makeText(LoginActivity.this, "Error processing server response", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Login failed: " + response.code(), Toast.LENGTH_SHORT).show();
            Log.e("LoginActivity", "Error: " + response.code() + " - " + response.message());
        }
    }private String extractJwtFromResponse(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            return jsonObject.optString("token", null);
        } catch (JSONException e) {
            Log.e("LoginActivity", "Error parsing JSON response", e);
            return null;
        }
    }

    private void saveJwt(String jwt) {
        getSharedPreferences("AppPreferences", MODE_PRIVATE)
                .edit()
                .putString("jwt_token", jwt)
                .apply();
        Log.i("LoginActivity", "JWT saved successfully");
    }
}