package com.example.tution_management_system_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AttendanceMain extends AppCompatActivity {
    Button addoredit_attend_btn,view_attend_btn,delete_attend_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Manage Attendance");
        setContentView(R.layout.activity_attendance_main);

        addoredit_attend_btn = findViewById(R.id.addoredit_attend_btn);
        addoredit_attend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceMain.this,Add_Attendence.class);
                startActivity(intent);
            }
        });

        view_attend_btn = findViewById(R.id.view_attend_btn);
        view_attend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceMain.this,Admin_View_Attendence.class);
                startActivity(intent);
            }
        });

        delete_attend_btn = findViewById(R.id.delete_attend_btn);
        delete_attend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceMain.this,Delete_Attendence.class);
                startActivity(intent);
            }
        });




    }
}