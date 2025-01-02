package com.example.totgether;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.totgether.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int DELAY_MILLIS = 3000; // Время задержки в миллисекундах

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity started, starting the delay");

        // Используем лямбда-выражение для сокращения кода
        new Handler().postDelayed(() -> {
            Log.d(TAG, "Transitioning to LoginActivity");

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish(); // Закрыть MainActivity
        }, DELAY_MILLIS);
    }
}