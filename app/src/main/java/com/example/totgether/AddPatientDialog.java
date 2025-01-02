package com.example.totgether;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddPatientDialog extends Dialog {

    private EditText editTextPatientName;
    private EditText editTextPatientTime;
    private EditText editTextPatientRoom;
    private Button buttonAddPatient;
    private FirebaseFirestore db;

    public interface OnPatientAddedListener {
        void onPatientAdded(String name, String time, String room);
    }

    private OnPatientAddedListener listener;

    public AddPatientDialog(@NonNull Context context, OnPatientAddedListener listener) {
        super(context);
        this.listener = listener; // Сохраняем слушателя
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient); // Убедитесь, что у вас есть этот файл

        editTextPatientName = findViewById(R.id.editTextPatientName);
        editTextPatientTime = findViewById(R.id.editTextPatientTime);
        editTextPatientRoom = findViewById(R.id.editTextPatientRoom);
        buttonAddPatient = findViewById(R.id.buttonAddPatient);

        buttonAddPatient.setOnClickListener(v -> {
            String name = editTextPatientName.getText().toString().trim();
            String time = editTextPatientTime.getText().toString().trim();
            String room = editTextPatientRoom.getText().toString().trim();

            if (name.isEmpty()  ||time.isEmpty() ||  room.isEmpty()) {
                Toast.makeText(getContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            savePatientToFirestore(name, time, room);
        });
    }

    private void savePatientToFirestore(String name, String time, String room) {
        Patient patient = new Patient(name, time, room);
        db.collection("patients")
                .add(patient)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Пациент добавлен: " + name, Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onPatientAdded(name, time, room); // Передаем данные обратно
                    }
                    dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}