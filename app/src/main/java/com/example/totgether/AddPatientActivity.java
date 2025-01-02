package com.example.totgether;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddPatientActivity extends AppCompatActivity {

    private EditText editTextPatientName;
    private EditText editTextPatientTime;
    private EditText editTextPatientRoom;
    private Button buttonAddPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        editTextPatientName = findViewById(R.id.editTextPatientName);
        editTextPatientTime = findViewById(R.id.editTextPatientTime);
        editTextPatientRoom = findViewById(R.id.editTextPatientRoom);
        buttonAddPatient = findViewById(R.id.buttonAddPatient);

        buttonAddPatient.setOnClickListener(v -> {
            try {
                String name = editTextPatientName.getText().toString().trim();
                String time = editTextPatientTime.getText().toString().trim();
                String room = editTextPatientRoom.getText().toString().trim();

                // Проверка на пустые поля
                if (name.isEmpty() || time.isEmpty() || room.isEmpty()) {
                    Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("patient_name", name);
                resultIntent.putExtra("patient_time", time);
                resultIntent.putExtra("patient_room", room);
                setResult(RESULT_OK, resultIntent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Произошла ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}