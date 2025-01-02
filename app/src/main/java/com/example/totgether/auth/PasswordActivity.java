package com.example.totgether.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.totgether.R;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        EditText passwordEditText = findViewById(R.id.passwordEditText);
        CheckBox rememberPasswordCheckbox = findViewById(R.id.rememberPasswordCheckbox);
        Button continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Логика обработки пароля и перехода на следующий экран
                String password = passwordEditText.getText().toString();

                // Валидация пароля
                if (password.length() >= 6) {
                    // Переход на экран "Добро пожаловать"
                    Intent intent = new Intent(PasswordActivity.this,  RegistrationCompleteActivity.class);
                    startActivity(intent);
                    finish();  // Завершить текущую активность
                } else {
                    // Показать сообщение об ошибке
                    Toast.makeText(PasswordActivity.this, "Пароль должен содержать 6 или более символов", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}