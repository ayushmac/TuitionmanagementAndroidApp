package com.example.tution_management_system_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class AdminMain extends AppCompatActivity {
    private Button logoutbtn;
    private Button add_stud_btn,add_notice_btn,pay_details_btn,attend_details_btn;
    private SharedPreferences sharedPreferences;
    //shared preference variable
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_USERNAME = "adminusrname";
    private static final String KEY_PSWD = "adminpswd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Admin Home");
        setContentView(R.layout.activity_admin_main);

        logoutbtn = findViewById(R.id.btn_logout);
        add_stud_btn = findViewById(R.id.add_stud_btn);
        add_notice_btn = findViewById(R.id.btn_notice);
        pay_details_btn = findViewById(R.id.pay_details_btn);
        attend_details_btn = findViewById(R.id.btn_attendance);
        //logout
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String adusrname = sharedPreferences.getString(KEY_USERNAME,null);
        String adpswd = sharedPreferences.getString(KEY_PSWD,null);

    logoutbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Toast.makeText(AdminMain.this,"Logged Out Successfully !",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminMain.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    });

    //edit student details i.e perform crud
    add_stud_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(AdminMain.this,Admin_Crud.class);
            startActivity(intent);
        }
    });

    add_notice_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(AdminMain.this,Notice_Crud.class);
            startActivity(intent);
        }
    });

        pay_details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMain.this,Admin_Check_Payment_History.class);
                startActivity(intent);
            }
        });

        attend_details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMain.this,AttendanceMain.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this).setMessage("Are your sure you want to exit ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton("No",null).show();

    }

}