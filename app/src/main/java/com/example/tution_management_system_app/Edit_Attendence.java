package com.example.tution_management_system_app;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Edit_Attendence extends AppCompatActivity {

    private ArrayList<String> arrayList = new ArrayList<>();
    String[] status = {"present","absent"};
    private Button attend_submit_btn;
    TextView stud_edit_id,stud_edit_name,stud_edit_doa,status2;
    AutoCompleteTextView stud_edit_spinner;
    ArrayAdapter<String> adapterItems;
    String status1,attend_id;
    Button attend_edit_submit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_attendence);
        String name = getIntent().getStringExtra("Student_name");
        String id = getIntent().getStringExtra("Student_id");
        String date = getIntent().getStringExtra("Date");

        stud_edit_id = findViewById(R.id.stud_edit_id);
        stud_edit_name = findViewById(R.id.stud_edit_name);
        stud_edit_doa = findViewById(R.id.stud_edit_doa);

        stud_edit_id.setText(id);
        stud_edit_name.setText(name);
        stud_edit_doa.setText(date);

        //present or absent
        stud_edit_spinner = findViewById(R.id.stud_edit_spinner);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,status);


        //fetching attendance id and status of student
        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Attendence Details").child(id);
        dataref.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               status1 = String.valueOf(snapshot.child("status").getValue());
               attend_id = String.valueOf(snapshot.child("attendance_id").getValue());
               stud_edit_spinner.setText(status1);
               stud_edit_spinner.setAdapter(adapterItems);
                stud_edit_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        attend_edit_submit_btn = findViewById(R.id.attend_edit_submit_btn);
        attend_edit_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //making changes in database
                DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Attendence Details").child(id).child(date);
                DatabaseReference dataref2 = FirebaseDatabase.getInstance().getReference("Attendence Recieved Details").child(attend_id);

                HashMap hashMap = new HashMap();
                hashMap.put("attendance_id",attend_id);
                hashMap.put("date",date);
                hashMap.put("status",stud_edit_spinner.getText().toString().trim());
                hashMap.put("student_id",id);
                hashMap.put("student_name",name);

                dataref.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Edit_Attendence.this,"Data Updated Successfully !",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Edit_Attendence.this,"Data Updation Failure !",Toast.LENGTH_LONG).show();
                    }
                });

                dataref2.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Edit_Attendence.this,"Data Updated Successfully !",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Edit_Attendence.this,"Data Updation Failure !",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });




    }

}