package com.example.tution_management_system_app;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.tution_management_system_app.attendance.Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_View_Attendence extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userId = user.getUid();
    RecyclerView recyclerView;
    EditText searchdate;
    DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference("Attendence Details").child(userId);
    Adapter adapter;
    ArrayList<AttendanceDetails> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Check Attendance");
        setContentView(R.layout.activity_student_view_attendence);
        recyclerView = findViewById(R.id.recyclerViewStudAttend1);
        searchdate = findViewById(R.id.searchAttendance);
        getData();
    }

    private void getData(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//       //new LinearLayoutManager(this)
        list = new ArrayList<>();
        adapter = new com.example.tution_management_system_app.attendance.Adapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                list.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    AttendanceDetails model = snapshot.getValue(AttendanceDetails.class);
                    //FeeDetails model = datasnapshot.getValue(FeeDetails.class);
                    list.add(model);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString().trim());
            }
        });


    }

    private void filter(String text) {
        ArrayList<AttendanceDetails> filterlist = new ArrayList<>();
        for(AttendanceDetails item : list){
            if(item.getDate().toLowerCase().contains(text.toLowerCase())){
                filterlist.add(item);
            }
        }
        adapter.Filteredlist(filterlist);
    }
}