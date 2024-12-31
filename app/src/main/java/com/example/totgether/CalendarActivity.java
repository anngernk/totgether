package com.example.totgether;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
//import com.google.firebase.crashlytics.buildtools;
import com.google.gson.Gson;
//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import android.widget.CalendarView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView patientRecyclerView;
    private PatientAdapter patientAdapter;
    private List<Patient> patientList;
    private TextView noAppointmentsTextView;
    private CalendarView calendarView;
    public String userId;

    private static final int ADD_PATIENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        userId = getIntent().getStringExtra("userId");
        patientRecyclerView = findViewById(R.id.patientRecyclerView);
        noAppointmentsTextView = findViewById(R.id.noAppointmentsTextView);
        calendarView = findViewById(R.id.calendarView);
        FloatingActionButton addPatientButton = findViewById(R.id.addPatientButton);

        patientList = new ArrayList<>();
        patientAdapter = new PatientAdapter(patientList);
        patientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientRecyclerView.setAdapter(patientAdapter);

        addPatientButton.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, AddPatientActivity.class);
            intent.putExtra("userId", userId);
            startActivityForResult(intent, ADD_PATIENT_REQUEST);
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            loadPatientsForDate(year, month, dayOfMonth);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PATIENT_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra("patient_name");
            String time = data.getStringExtra("patient_time");
            String room = data.getStringExtra("patient_room");
            patientList.add(new Patient(name, time, room));
            patientAdapter.notifyDataSetChanged();
        }
    }

    private void loadPatientsForDate(int year, int month, int dayOfMonth) {
        String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
        patientList.clear();
        loadPatientsFromServer(selectedDate);
    }

    private void loadPatientsFromServer(String selectedDate) {
        new Thread(() -> {
            try {
                URL url = new URL("jdbc:mysql://localhost:3306/totgether" + selectedDate);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    Gson gson = new Gson();
                    Type patientListType = new TypeToken<List<Patient>>(){}.getType();
                    List<Patient> patients = gson.fromJson(response.toString(), patientListType);


                    runOnUiThread(this::updateUI);
                } else {
                    runOnUiThread(() -> Toast.makeText(CalendarActivity.this, "Нет данных", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(CalendarActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void updateUI() {
        if (patientList.isEmpty()) {
            noAppointmentsTextView.setVisibility(View.VISIBLE);
        } else {
            noAppointmentsTextView.setVisibility(View.GONE);
        }
        patientAdapter.notifyDataSetChanged();
    }
}