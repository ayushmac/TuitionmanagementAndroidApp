package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student_Reset_EmailandPassword extends AppCompatActivity {
    TextInputEditText email,pswd;
    ProgressBar progressBar;
    Button stud_reset_pswd_btn,stud_reset_email_btn;
    TextInputLayout textInputLayoutresetpswd,textInputLayoutresetemail;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String userId = user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Reset E-mail or Password");
        setContentView(R.layout.activity_student_reset_emailand_password);

        email = findViewById(R.id.stud_reset_email);
        textInputLayoutresetemail = findViewById(R.id.textinputlayoutresetemail);
        textInputLayoutresetpswd = findViewById(R.id.textInputLayoutresetpswd);
        pswd = findViewById(R.id.stud_reset_password);
        stud_reset_pswd_btn = findViewById(R.id.stud_reset_pswd_btn);
        stud_reset_email_btn = findViewById(R.id.stud_reset_email_btn);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        //email validation
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usremail= email.getText().toString().trim();
                if(usremail.isEmpty()){
                    stud_reset_email_btn.setVisibility(View.GONE);
                    textInputLayoutresetemail.setHelperText("* Required field");
                    textInputLayoutresetemail.setBoxStrokeColor(Color.RED);
                }
                else if (!isEmail(usremail)){
                    stud_reset_email_btn.setVisibility(View.GONE);
                    textInputLayoutresetemail.setHelperText("Enter a valid email ! eg : youremail@example.com");
                    textInputLayoutresetemail.setBoxStrokeColor(Color.RED);
                }
                else{
                    stud_reset_email_btn.setVisibility(View.VISIBLE);
                    textInputLayoutresetemail.setHelperText("");
                    textInputLayoutresetemail.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usremail= email.getText().toString().trim();

                if(usremail.isEmpty()){
                    stud_reset_email_btn.setVisibility(View.GONE);
                    textInputLayoutresetemail.setHelperText("* Required field");
                    textInputLayoutresetemail.setBoxStrokeColor(Color.RED);
                }
                else if (!isEmail(usremail)){
                    stud_reset_email_btn.setVisibility(View.GONE);
                    textInputLayoutresetemail.setHelperText("Enter a valid email ! eg : youremail@example.com");
                    textInputLayoutresetemail.setBoxStrokeColor(Color.RED);
                }
                else{
                    stud_reset_email_btn.setVisibility(View.VISIBLE);
                    textInputLayoutresetemail.setHelperText("");
                    textInputLayoutresetemail.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usremail = email.getText().toString().trim();

                if(usremail.isEmpty()){
                    stud_reset_email_btn.setVisibility(View.GONE);
                    textInputLayoutresetemail.setHelperText("* Required field");
                    textInputLayoutresetemail.setBoxStrokeColor(Color.RED);
                }
                else if (!isEmail(usremail)){
                    stud_reset_email_btn.setVisibility(View.GONE);
                    textInputLayoutresetemail.setHelperText("Enter a valid email ! eg : youremail@example.com");
                    textInputLayoutresetemail.setBoxStrokeColor(Color.RED);
                }
                else{
                    stud_reset_email_btn.setVisibility(View.VISIBLE);
                    textInputLayoutresetemail.setHelperText("");
                    textInputLayoutresetemail.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }
        });

        //password
        pswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pswd1= pswd.getText().toString().trim();
                if(!pswd1.isEmpty()){
                    stud_reset_pswd_btn.setVisibility(View.VISIBLE);
                    textInputLayoutresetpswd.setHelperText("");
                    textInputLayoutresetpswd.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                else{
                    stud_reset_pswd_btn.setVisibility(View.GONE);
                    textInputLayoutresetpswd.setHelperText("* Required field");
                    textInputLayoutresetpswd.setBoxStrokeColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pswd1= pswd.getText().toString().trim();
                if(!pswd1.isEmpty()){
                    stud_reset_pswd_btn.setVisibility(View.VISIBLE);
                    textInputLayoutresetpswd.setHelperText("");
                    textInputLayoutresetpswd.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                else{
                    stud_reset_pswd_btn.setVisibility(View.GONE);
                    textInputLayoutresetpswd.setHelperText("* Required field");
                    textInputLayoutresetpswd.setBoxStrokeColor(Color.RED);
                }
            }
        });


        //reset email
        stud_reset_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEmail = email.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Map<String,Object> map = new HashMap<>();
                            map.put("email",newEmail);
                            FirebaseDatabase.getInstance().getReference().child("Student Details")
                                    .child(userId).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Student_Reset_EmailandPassword.this,"Email Updated in database Successfully !",Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Student_Reset_EmailandPassword.this,"Email Updation Failure in database !",Toast.LENGTH_LONG).show();

                                        }
                                    });
                            Toast.makeText(Student_Reset_EmailandPassword.this,"Email updated !",Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            Intent intent = new Intent(Student_Reset_EmailandPassword.this,MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else{
                            Toast.makeText(Student_Reset_EmailandPassword.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

        //reset password
        stud_reset_pswd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = pswd.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Map<String,Object> map = new HashMap<>();
                            map.put("password",newPassword);
                            FirebaseDatabase.getInstance().getReference().child("Student Details")
                                    .child(userId).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(Student_Reset_EmailandPassword.this,"Password Updated in database Successfully !",Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Student_Reset_EmailandPassword.this,"Password Updation Failure in database !",Toast.LENGTH_LONG).show();

                                        }
                                    });

                            Toast.makeText(Student_Reset_EmailandPassword.this,"Password updated !",Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            Intent intent = new Intent(Student_Reset_EmailandPassword.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else{
                            Toast.makeText(Student_Reset_EmailandPassword.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    public boolean isEmail(String in){
        String email_Regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(email_Regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(in);
        return matcher.find();
    }
}