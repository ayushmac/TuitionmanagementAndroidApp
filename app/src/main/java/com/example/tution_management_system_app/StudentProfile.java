package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends AppCompatActivity {

    FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String userId;
    CircleImageView profilepic;
    FirebaseUser user;
    ProgressBar progressBar;
    Button edit_stud_btn,reset_emailpswd_btn,delete_useracc_btn;
    TextView fullname,email,contact_1,contact_2;
    String PROFILE_IMAGE_URL= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_student_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Student Details");
        userId = user.getUid();
        profilepic = findViewById(R.id.profilepic);

        fullname = findViewById(R.id.namedisplaytxt);
        email = findViewById(R.id.emaildisplaytxt);
        contact_1 = findViewById(R.id.contact1tdisplaytxt);
        contact_2 = findViewById(R.id.contact2displaytxt);
        edit_stud_btn = findViewById(R.id.edit_profile_btn);
        reset_emailpswd_btn = findViewById(R.id.reset_email_pass_btn);
        delete_useracc_btn = findViewById(R.id.user_delete_acc_btn);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                StudentInserter suser = snapshot.getValue(StudentInserter.class);

                if(user != null){

                    String imagelink = suser.getImageUrl();
                    String fullnametxt = suser.getName();
                    String emailtxt = suser.getEmail();
                    String contact1txt = suser.getContact_no();
                    String contact2txt = suser.getContact_no_2();

                    //setting profile pic and details on page
                    Picasso.get().load(imagelink).into(profilepic);
                    fullname.setText(fullnametxt);
                    email.setText(emailtxt);
                    contact_1.setText(contact1txt);
                    contact_2.setText(contact2txt);
                }else{
                    Toast.makeText(StudentProfile.this,"User does not exist !",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(StudentProfile.this,MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentProfile.this,"Something wrong happenned !",Toast.LENGTH_LONG).show();
            }
        });

        edit_stud_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this,Student_Profile_Edit.class);
                startActivity(intent);
            }
        });

        reset_emailpswd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfile.this,Student_Reset_EmailandPassword.class);
                startActivity(intent);
            }
        });

        delete_useracc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(StudentProfile.this);
                dialog.setTitle("Are you sure ?");
                dialog.setMessage("Deleteing this account will result in completely removing your account from the system and you won't be able to access the app.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        /*
                        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                StudentInserter suser = snapshot.getValue(StudentInserter.class);

                                if(user != null){
                                    String imagelink = suser.getImageUrl();
                                    FirebaseStorage mStorage = FirebaseStorage.getInstance();
                                    StorageReference imageref = mStorage.getReferenceFromUrl(imagelink);
                                    imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(view.getContext(), "image deleted !",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }else{
                                    Toast.makeText(StudentProfile.this,"User does not exist !",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(StudentProfile.this,MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(StudentProfile.this,"Something wrong happenned !",Toast.LENGTH_LONG).show();
                            }
                        });*/


                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){/*
                                    FirebaseDatabase.getInstance().getReference().child("Student Details")
                                            .child(userId).removeValue();
                                    Toast.makeText(StudentProfile.this,"Details deleted successfully !",Toast.LENGTH_SHORT).show();*/
                                    Toast.makeText(StudentProfile.this,"Account Deleted !",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(StudentProfile.this,MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                                else{
                                    Toast.makeText(StudentProfile.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });










    }
}