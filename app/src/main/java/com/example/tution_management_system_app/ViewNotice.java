package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewNotice extends AppCompatActivity {
    TextView notice_title;
    TextView descriptcontent;
    Button delete_notice_btn;
    ImageView imageView;
    View v;
    DatabaseReference ref,DataRef;
    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_view_notice);
        notice_title = findViewById(R.id.notice_title_txt);
        imageView = findViewById(R.id.notice_image_single_view1);
        delete_notice_btn = findViewById(R.id.delete_notice);
        descriptcontent = findViewById(R.id.notice_descript_text);

        ref = FirebaseDatabase.getInstance().getReference().child("Notices");
        String NoticeKey = getIntent().getStringExtra("NoticeKey");
        DataRef = FirebaseDatabase.getInstance().getReference().child("Notices").child(NoticeKey);
        storageRef = FirebaseStorage.getInstance().getReference().child("NoticeImages").child(NoticeKey+".jpg");
        ref.child(NoticeKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String noticetitle = snapshot.child("notice_title").getValue().toString();
                    String imageUrl = snapshot.child("imageUrl").getValue().toString();
                    String noticedescript = snapshot.child("notice_description").getValue().toString();
                    Picasso.get().load(imageUrl).into(imageView);
                    notice_title.setText(noticetitle);
                    descriptcontent.setText(noticedescript);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete_notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewNotice.this);
                dialog.setTitle("Are you sure ?");
                dialog.setMessage("Deleting this post will result in completely removing your post from the system.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ViewNotice.this,"Notice deleted successfully !",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(),Notice_Crud.class));
                                        finish();
                                    }
                                });
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

    }
}