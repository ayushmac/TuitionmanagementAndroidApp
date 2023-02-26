package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class Add_Attendence extends AppCompatActivity {
    Spinner attend_status,studentlist;
    TextView txtview;
    TextInputEditText doa;
    TextInputLayout attend_status_textInputLayout;
    private DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference dataref1 = FirebaseDatabase.getInstance().getReference("Attendence Details");
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> uid = new ArrayList<String>();
    String[] status = {"present","absent"};
    private Button attend_submit_btn,attend_edit_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_attendence);
        txtview = findViewById(R.id.stud_edit_id);
        doa = findViewById(R.id.date_of_attend);
        attend_status_textInputLayout = findViewById(R.id.doa_textInputLayout);

        //date picker for attendence
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                calendar.set(Calendar.DAY_OF_MONTH,dayofmonth);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.YEAR,year);
                updateCalendar();
            }

            private void updateCalendar(){
                String Format = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
                doa.setText(sdf.format(calendar.getTime()));
            }
        };

        doa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Add_Attendence.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String Format = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
        doa.setText(sdf.format(calendar.getTime()));

        //present or absent
        attend_status = findViewById(R.id.stud_edit_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_Attendence.this,R.layout.list_item,status);
        adapter.setDropDownViewResource(R.layout.list_item);
        attend_status.setAdapter(adapter);
        attend_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //pick users from firebase
        studentlist = findViewById(R.id.spinner);
        showDataSpinner();
        studentlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();
                String value2 = uid.get(i);
                txtview.setText(value2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        //submit buttons
        attend_submit_btn = findViewById(R.id.attend_edit_submit_btn);
        attend_edit_btn = findViewById(R.id.attend_edit_btn);

        attend_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aid = dataref1.push().getKey();
                HashMap hashMap = new HashMap();
                hashMap.put("student_name",studentlist.getSelectedItem().toString().trim());
                hashMap.put("status",attend_status.getSelectedItem().toString().trim());
                hashMap.put("date",doa.getText().toString().trim());
                hashMap.put("student_id",txtview.getText().toString().trim());
                hashMap.put("attendance_id",aid);

                DatabaseReference dataref2 = FirebaseDatabase.getInstance().getReference("Attendence Details").child(txtview.getText().toString().trim());
                DatabaseReference dataref3 = FirebaseDatabase.getInstance().getReference("Attendence Recieved Details");
                String date1 = doa.getText().toString().trim();
                dataref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(doa.getText().toString().trim())){
                            Snackbar.make(findViewById(R.id.attendenceaddlayout),"You've already marked this user's attendance on "+doa.getText().toString().trim()+".Click on edit button to edit this user's attendance.",Snackbar.LENGTH_LONG).setTextMaxLines(10).show();
                        }
                        else{
                            //attendance inserted in master table
                            dataref1.child(txtview.getText().toString()).child(doa.getText().toString()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Add_Attendence.this,"Data Inserted Successfully !",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Add_Attendence.this,e.getMessage().toString().trim(),Toast.LENGTH_LONG).show();
                                }
                            });

                            //attendance inserted in admin table for viewing and editing
                            dataref3.child(aid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Add_Attendence.this,"Data sent to admin successfully !",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Add_Attendence.this,"Some error occured! Admin didn't recieve your attendance !",Toast.LENGTH_LONG).show();
                                }
                            });



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


        attend_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date1 = doa.getText().toString().trim();
                DatabaseReference dataref2 = FirebaseDatabase.getInstance().getReference("Attendence Details").child(txtview.getText().toString().trim());
                dataref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(doa.getText().toString().trim())){
                            Intent intent = new Intent(Add_Attendence.this,Edit_Attendence.class);
                            intent.putExtra("Student_id",txtview.getText().toString());
                            intent.putExtra("Student_name",studentlist.getSelectedItem().toString().trim());
                            intent.putExtra("Date",doa.getText().toString());
                            //String NoticeKey = getIntent().getStringExtra("NoticeKey");
                            startActivity(intent);
                        }
                        else{
                            Snackbar.make(findViewById(R.id.attendenceaddlayout),"Student attendance not present on this date ! First add attendance !",Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });




    }

    private void showDataSpinner() {
        dataref.child("Student Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot item : snapshot.getChildren()){
                    arrayList.add(item.child("name").getValue(String.class));
                    String ids = item.child("sid").getValue(String.class);
                    uid.add(ids);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Add_Attendence.this,R.layout.list_item,arrayList);
                studentlist.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}