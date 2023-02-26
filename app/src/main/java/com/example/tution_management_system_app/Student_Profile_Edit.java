package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Student_Profile_Edit extends AppCompatActivity {
    TextInputEditText sname,semail,sdob,scon1,scon2,sadd,susername,spassword;
    Button edit_btn;
    AutoCompleteTextView sgrade;
    FirebaseAuth mAuth;
    private DatabaseReference reference;
    FirebaseUser user;
    private String userId;
    String[] items = {"Jr.Kg","Sr.Kg","1st","2nd","3rd","4th","5th","6th","7th","8th"};
    ProgressBar progressBar ;
    ArrayAdapter<String> adapterItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_student_profile_edit);


        sname = findViewById(R.id.edit_stud_name);
        semail = findViewById(R.id.edit_stud_email);
        progressBar = findViewById(R.id.progressnoticeupload);
        sdob = findViewById(R.id.edit_stud_dob);
        //date picker for d.o.b
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayofmonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, year);

                updateCalendar();
            }

            private void updateCalendar() {
                String Format = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
                sdob.setText(sdf.format(calendar.getTime()));
            }
        };

        sdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Student_Profile_Edit.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        scon1 = findViewById(R.id.edit_stud_contact);
        scon2 = findViewById(R.id.edit_stud_contact2);

        sgrade = findViewById(R.id.edit_stud_grade);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);


        sadd = findViewById(R.id.edit_stud_address);
        susername = findViewById(R.id.edit_stud_username);
        spassword = findViewById(R.id.edit_stud_password);
        edit_btn = findViewById(R.id.edit_stud_submit_btn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Student Details");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                StudentInserter suser = snapshot.getValue(StudentInserter.class);

                if(user != null){
                    String fullnametxt = suser.getName();
                    String emailtxt = suser.getEmail();
                    String dobtxt = suser.getDob();
                    String contact1txt = suser.getContact_no();
                    String contact2txt = suser.getContact_no_2();
                    String gradetxt = suser.getGrade();
                    String usernametxt = suser.getUsername();
                    String addresstxt = suser.getAddress();
                    String passwordtxt = suser.getPassword();

                    //setting profile pic and details on page
                    sname.setText(fullnametxt);
                    semail.setText(emailtxt);
                    sdob.setText(dobtxt);
                    scon1.setText(contact1txt);
                    scon2.setText(contact2txt);
                    sgrade.setText(gradetxt);
                    sgrade.setAdapter(adapterItems);

                    sgrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String item = parent.getItemAtPosition(position).toString();
                        }
                    });
                    susername.setText(usernametxt);
                    spassword.setText(passwordtxt);
                    sadd.setText(addresstxt);


                    semail.setEnabled(false);
                    spassword.setEnabled(false);
                    progressBar.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Student_Profile_Edit.this,"Something wrong happenned !",Toast.LENGTH_LONG).show();
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                Map<String,Object> map = new HashMap<>();
                map.put("name",sname.getText().toString().trim());
                map.put("email",semail.getText().toString().trim());
                map.put("contact_no",scon1.getText().toString().trim());
                map.put("contact_no_2",scon2.getText().toString().trim());
                map.put("dob",sdob.getText().toString().trim());
                map.put("grade",sgrade.getText().toString().trim());
                map.put("address",sadd.getText().toString().trim());
                map.put("username",susername.getText().toString().trim());
                map.put("password",spassword.getText().toString().trim());

                FirebaseDatabase.getInstance().getReference().child("Student Details")
                        .child(userId).updateChildren(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Student_Profile_Edit.this,"Data Updated Successfully !",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(Student_Profile_Edit.this,StudentMain.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Student_Profile_Edit.this,"Data Updation Failure !",Toast.LENGTH_LONG).show();

                            }
                        });

            }
        });
    }
}