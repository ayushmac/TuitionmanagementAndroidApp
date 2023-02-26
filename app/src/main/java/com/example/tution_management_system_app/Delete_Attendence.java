package com.example.tution_management_system_app;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Delete_Attendence extends AppCompatActivity {
    Spinner attend_status,studentlist;
    TextView txtview;
    TextInputEditText doa;
    private Button delete_att_btn;
    private String aid;

    private DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference dataref1 = FirebaseDatabase.getInstance().getReference("Attendence Details");
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> uid = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_delete_attendence);
        txtview = findViewById(R.id.stud_edit_id);
        doa = findViewById(R.id.date_of_attend);

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
                new DatePickerDialog(Delete_Attendence.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        String Format = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
        doa.setText(sdf.format(calendar.getTime()));

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

        delete_att_btn = findViewById(R.id.delete_att_btn);
        delete_att_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Delete_Attendence.this).setTitle("Read Carefully !").setMessage("Are you sure you want to delete ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get aid first
                        DatabaseReference dataref1 = FirebaseDatabase.getInstance().getReference("Attendence Details").child(txtview.getText().toString().trim());
                        dataref1.child(doa.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                aid = String.valueOf(snapshot.child("attendance_id").getValue());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        DatabaseReference dataref2 = FirebaseDatabase.getInstance().getReference("Attendence Details").child(txtview.getText().toString().trim());
                        dataref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(doa.getText().toString().trim())){
                                    // delete in attendence details
                                    FirebaseDatabase.getInstance().getReference().child("Attendence Details")
                                            .child(txtview.getText().toString().trim()).child(doa.getText().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Delete_Attendence.this, "Attendance Details deleted !",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    //delete in attendence recieved
                                    FirebaseDatabase.getInstance().getReference().child("Attendence Recieved Details").child(aid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Delete_Attendence.this, "Attendance Details deleted !",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
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
                }).setNegativeButton("No",null).show();
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

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Delete_Attendence.this,R.layout.list_item,arrayList);
                studentlist.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}