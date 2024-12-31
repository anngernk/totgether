package com.example.totgether.auth;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.totgether.ApiClient;
import com.example.totgether.ApiService;
import com.example.totgether.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            String login = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, введите электронную почту и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Пароль должен содержать хотя бы 6 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(login, password);
        });
    }

    private void registerUser(String login, String password) {
        ApiService apiService = ApiClient.getApiService();
        Log.i("RegisterActivity","end point called");
        Call<ResponseBody> call = apiService.registerUser(new LoginRequest(login,password));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        handleServerResponse(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("RegisterActivity", t.getMessage());
                Toast.makeText(RegisterActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleServerResponse(String response) {
        if (response.contains("Регистрация успешна")) {
            Toast.makeText(RegisterActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
            finish(); // Закрытие активности
        } else {
            Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + response, Toast.LENGTH_SHORT).show();
        }
    }
}