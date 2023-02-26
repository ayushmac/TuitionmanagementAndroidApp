package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentLogin extends AppCompatActivity {
    private TextInputEditText studusrname,studpswd;
    private TextInputLayout studusernamelayout,studpswdlayout;
    Button studloginbtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_student_login);
        studusrname = findViewById(R.id.amount);
        studpswd = findViewById(R.id.studpswd);
        studloginbtn = findViewById(R.id.studloginbtn);
        studusernamelayout = findViewById(R.id.textinputlayouttitle);
        studpswdlayout = findViewById(R.id.textInputLayoutresetpswd);

        studusrname.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usremail= studusrname.getText().toString().trim();

                if(usremail.isEmpty()){
                    //submitbtn.setVisibility(View.GONE);
                    studusernamelayout.setHelperText("* Required field");
                    studusernamelayout.setBoxStrokeColor(Color.RED);
                }
                else if (!isEmail(usremail)){
                    //submitbtn.setVisibility(View.GONE);
                    studusernamelayout.setHelperText("Enter a valid email ! eg : youremail@example.com");
                    studusernamelayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    studusernamelayout.setHelperText("");
                    studusernamelayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usremail = studusrname.getText().toString().trim();

                if(usremail.isEmpty()){
                    //submitbtn.setVisibility(View.GONE);
                    studusernamelayout.setHelperText("* Required field");
                    studusernamelayout.setBoxStrokeColor(Color.RED);
                }
                else if (!isEmail(usremail)){
                    //.setVisibility(View.GONE);
                    studusernamelayout.setHelperText("Enter a valid email ! eg : youremail@example.com");
                    studusernamelayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    studusernamelayout.setHelperText("");
                    studusernamelayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }
        });

        studpswd.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usrpswd= studpswd.getText().toString().trim();
                if(usrpswd.isEmpty()){
                    studpswdlayout.setHelperText("* Required field");
                    studpswdlayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    studpswdlayout.setHelperText("");
                    studpswdlayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrpswd= studpswd.getText().toString().trim();
                if(usrpswd.isEmpty()){
                    studpswdlayout.setHelperText("* Required field");
                    studpswdlayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    studpswdlayout.setHelperText("");
                    studpswdlayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar4);
        signup = findViewById(R.id.newusertxt);
        studloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentLogin.this,Add_Student.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(){
        String email = studusrname.getText().toString().trim();
        String password = studpswd.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            studusernamelayout.setBoxStrokeColor(Color.RED);
            studusernamelayout.setHelperText("Email cannot be empty !");
            studusrname.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            studpswdlayout.setBoxStrokeColor(Color.RED);
            studpswdlayout.setHelperText("Password cannot be empty !");
            studpswd.requestFocus();
        }
        else{
            studusernamelayout.setHelperText("");
            studpswdlayout.setHelperText("");
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(StudentLogin.this,"Logged in Sucessfully !",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(StudentLogin.this,StudentMain.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                    else{
                        Toast.makeText(StudentLogin.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    public boolean isEmail(String in){
        String email_Regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(email_Regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(in);
        return matcher.find();
    }



}