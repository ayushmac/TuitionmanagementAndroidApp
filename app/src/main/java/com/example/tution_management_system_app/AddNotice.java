package com.example.tution_management_system_app;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddNotice extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101 ;
    private Button submitnotice;
    private TextInputEditText ntitle,ndescrpt;
    private ImageView noticeimageView;
    ProgressBar progressBar;
    Uri imageUri;
    boolean isImageAdded = false;
    DatabaseReference DataRef;
    StorageReference StorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Notice");
        setContentView(R.layout.activity_add_notice);
        noticeimageView = findViewById(R.id.noticeimage);
        submitnotice = findViewById(R.id.notice_submit_btn);
        ntitle = findViewById(R.id.notice_title);
        ndescrpt = findViewById(R.id.notice_description);
        progressBar = findViewById(R.id.progressnoticeupload);
        progressBar.setVisibility(View.INVISIBLE);

        DataRef = FirebaseDatabase.getInstance().getReference().child("Notices");
        StorageRef = FirebaseStorage.getInstance().getReference().child("NoticeImages");
        submitnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String imageName = ntitle.getText().toString().trim();
                if(isImageAdded != false && imageName!= null){
                    uploadImage(imageName);

                }
                else{
                    Toast.makeText(AddNotice.this,"please Add an Image !",Toast.LENGTH_LONG).show();
                }
            }
        });



        noticeimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

    }

    private void uploadImage(final String imageName){

        final String key = DataRef.push().getKey();
        String noticetitletxt = ntitle.getText().toString().trim();
        String noticedescriptxt = ndescrpt.getText().toString().trim();
        StorageRef.child(key+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageRef.child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("notice_id",key);
                        hashMap.put("notice_title",noticetitletxt);
                        hashMap.put("notice_description",noticedescriptxt);
                        hashMap.put("imageUrl",uri.toString());
                        DataRef.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddNotice.this,"Data Inserted Successfully !",Toast.LENGTH_LONG).show();
                                noticeimageView.setImageResource(R.drawable.ic_addphoto_foreground);
                                ntitle.setText("");
                                ndescrpt.setText("");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_IMAGE && data!=null){
            imageUri = data.getData();
            isImageAdded = true;
            noticeimageView.setImageURI(imageUri);
        }
    }
}