package com.example.totgether;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AppointmentDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        Button writePatientButton = findViewById(R.id.writePatientButton);
        Button cancelAppointmentButton = findViewById(R.id.cancelAppointmentButton);

        writePatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppointmentDetailsActivity.this, "Сообщение отправлено", Toast.LENGTH_SHORT).show();
            }
        });

        cancelAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppointmentDetailsActivity.this, "Прием отменен", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}