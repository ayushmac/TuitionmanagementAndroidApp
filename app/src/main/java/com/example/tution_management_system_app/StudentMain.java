package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentMain extends AppCompatActivity {
    private Button btnlogout;
    private Button viewprofilebtn,payfeesbtn,chknotice,chkattend;
    private DatabaseReference reference;
    private String userId;
    FirebaseUser user;
    TextView greet;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Student Home");
        setContentView(R.layout.activity_student_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Student Details");
        userId = user.getUid();
        greet = findViewById(R.id.greettxt);

        //greet user on dash board
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentInserter suser = snapshot.getValue(StudentInserter.class);

                if(user != null){
                    String username = suser.getUsername();
                    greet.setText("Welcome! "+username);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentMain.this,"Something wrong happenned !",Toast.LENGTH_LONG).show();
            }
        });

        //logout
        btnlogout = findViewById(R.id.stud_logout_btn);
        mAuth = FirebaseAuth.getInstance();
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(StudentMain.this,MainActivity.class));
            }
        });

        //View Profile
        viewprofilebtn = findViewById(R.id.viewprofilebtn);
        viewprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMain.this,StudentProfile.class));
            }
        });

        payfeesbtn = findViewById(R.id.pay_fees_btn);
        payfeesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMain.this,Pay_Fees_Page.class));
            }
        });

        chknotice = findViewById(R.id.chk_notice_btn);
        chknotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMain.this,StudentNotice.class));
            }
        });

        chkattend = findViewById(R.id.check_attend_btn);
        chkattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMain.this,Student_View_Attendence.class));
            }
        });





    }


}