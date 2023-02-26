package com.example.tution_management_system_app;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.*;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Add_Student extends AppCompatActivity {
    ImageView profilepic;
    private Button submitbtn;
    ProgressBar progressupload;
    TextInputEditText name,email,dob,contactno,contactno2,address,username,password;
    TextInputLayout stud_name_textInputLayout,stud_email_textInputLayout,stud_dob_textInputLayout,
            stud_contact1_textInputLayout,stud_contact2_textInputLayout,stud_grade_textInputLayout,
            stud_add_textInputLayout,stud_usrname_textInputLayout,stud_pswd_textInputLayout;

    AutoCompleteTextView grade;
    Calendar calendar;
    private Uri imageUri;
    private TextView text;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Student Details");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    FirebaseAuth mauth;
    ActivityResultLauncher<String> mGetContent;
    String[] items = {"Jr.Kg","Sr.Kg","1st","2nd","3rd","4th","5th","6th","7th","8th"};
    ArrayAdapter<String> adapterItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Student details");
        setContentView(R.layout.activity_add_student);

        submitbtn = findViewById(R.id.stud_submit_btn);
        profilepic = findViewById(R.id.stud_profile_pic);
        progressupload = findViewById(R.id.progress_stud_upload);
        progressupload.setVisibility(View.INVISIBLE);
        name = findViewById(R.id.stud_name);
        email = findViewById(R.id.stud_email);
        dob = findViewById(R.id.date_of_attend);
        contactno = findViewById(R.id.stud_contact1);
        contactno2 = findViewById(R.id.stud_contact2);
        grade = findViewById(R.id.stud_grade);
        address = findViewById(R.id.stud_address);

        stud_name_textInputLayout = findViewById(R.id.stud_name_textInputLayout);
        stud_email_textInputLayout = findViewById(R.id.stud_email_textInputLayout);
        stud_dob_textInputLayout = findViewById(R.id.stud_dob_textInputLayout);
        stud_contact1_textInputLayout = findViewById(R.id.stud_contact1_textInputLayout);
        stud_contact2_textInputLayout = findViewById(R.id.stud_contact2_textInputLayout);
        stud_grade_textInputLayout = findViewById(R.id.stud_grade_textInputLayout);
        stud_add_textInputLayout = findViewById(R.id.stud_add_textInputLayout);
        stud_usrname_textInputLayout = findViewById(R.id.stud_usrname_textInputLayout);
        stud_pswd_textInputLayout = findViewById(R.id.stud_pswd_textInputLayout);

        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
        grade.setAdapter(adapterItems);
        grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });
        address = findViewById(R.id.stud_address);
        username = findViewById(R.id.stud_username);
        password = findViewById(R.id.stud_pswd);
        text =findViewById(R.id.clickhere);



        //date picker for d.o.b
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                calendar.set(Calendar.DAY_OF_MONTH,dayofmonth);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.YEAR,year);
                updateCalendar();
            }

            private void updateCalendar(){
                String Format = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
                dob.setText(sdf.format(calendar.getTime()));
            }
        };



        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Add_Student.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //setting profile pic on imageview
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                      imageUri = result;
                      profilepic.setImageURI(imageUri);
                      text.setText("");
                    }
        });

        //click on image view to launch gallery
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
                text.setText("");
            }
        });


        submitbtn.setVisibility(View.GONE);

        //upload btn
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usrdob = dob.getText().toString().trim();
                String grades = grade.getText().toString().trim();

                //photo validation
                if(imageUri != null){
                    uploadPhotoToFirebase(imageUri);
                    stud_dob_textInputLayout.setHelperText("");
                    stud_dob_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                    stud_grade_textInputLayout.setHelperText("");
                    stud_grade_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                else {
                    Toast.makeText(Add_Student.this, "Please add a photo !", Toast.LENGTH_SHORT).show();
                }
                //dob validation
                if(usrdob.isEmpty()){
                    dob.requestFocus();
                    stud_dob_textInputLayout.setHelperText("* Required field");
                    stud_dob_textInputLayout.setBoxStrokeColor(Color.RED);
                }

                // grade validation
                if(grades.isEmpty()){
                    grade.requestFocus();
                    stud_grade_textInputLayout.setHelperText("* Required field");
                    stud_grade_textInputLayout.setBoxStrokeColor(Color.RED);
                }

            }
        });

        //validations
        name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usrname = name.getText().toString().trim();

                if(usrname.isEmpty()){
                    stud_name_textInputLayout.setHelperText("* Required field");
                    stud_name_textInputLayout.setBoxStrokeColor(Color.RED);
                    submitbtn.setVisibility(View.GONE);
                }
                else if (!isWord(usrname)){;
                    stud_name_textInputLayout.setHelperText("Enter a valid name ! should not include symbols or numbers !");
                    stud_name_textInputLayout.setBoxStrokeColor(Color.RED);
                    submitbtn.setVisibility(View.GONE);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_name_textInputLayout.setHelperText("");
                    stud_name_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrname = name.getText().toString().trim();
                if(usrname.isEmpty()){
                   submitbtn.setVisibility(View.GONE);
                    stud_name_textInputLayout.setHelperText("* Required field");
                    stud_name_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else if (!isWord(usrname)){
                    stud_name_textInputLayout.requestFocus();
                    stud_name_textInputLayout.setHelperText("Enter a valid name ! should not include symbols or numbers !");
                    stud_name_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_name_textInputLayout.setHelperText("");
                    stud_name_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }
        });

        //email validation
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usremail= email.getText().toString().trim();

                if(usremail.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_email_textInputLayout.setHelperText("* Required field");
                    stud_email_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else if (!isEmail(usremail)){
                    submitbtn.setVisibility(View.GONE);
                    stud_email_textInputLayout.setHelperText("Enter a valid email ! eg : youremail@example.com");
                    stud_email_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_email_textInputLayout.setHelperText("");
                    stud_email_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usremail= email.getText().toString().trim();

                if(usremail.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_email_textInputLayout.setHelperText("* Required field");
                    stud_email_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else if (!isEmail(usremail)){
                    submitbtn.setVisibility(View.GONE);
                    stud_email_textInputLayout.setHelperText("Enter a valid email ! eg : youremail@example.com");
                    stud_email_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_email_textInputLayout.setHelperText("");
                    stud_email_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usremail = email.getText().toString().trim();

                if(usremail.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_email_textInputLayout.setHelperText("* Required field");
                    stud_email_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else if (!isEmail(usremail)){
                    submitbtn.setVisibility(View.GONE);
                    stud_email_textInputLayout.setHelperText("Enter a valid email ! eg : youremail@example.com");
                    stud_email_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_email_textInputLayout.setHelperText("");
                    stud_email_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }
        });

        //dob validation
        dob.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usrdob = dob.getText().toString().trim();

                if(usrdob.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_dob_textInputLayout.setHelperText("* Required field");
                    stud_dob_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_dob_textInputLayout.setHelperText("");
                    stud_dob_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usrdob = dob.getText().toString().trim();

                if(usrdob.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_dob_textInputLayout.setHelperText("* Required field");
                    stud_dob_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_dob_textInputLayout.setHelperText("");
                    stud_dob_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrdob = dob.getText().toString().trim();

                if(usrdob.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_dob_textInputLayout.setHelperText("* Required field");
                    stud_dob_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_dob_textInputLayout.setHelperText("");
                    stud_dob_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }
        });


               //contact1 validation
        contactno.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String contact1 = contactno.getText().toString().trim();
                if(contact1.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_contact1_textInputLayout.setHelperText("* Required field");
                    stud_contact1_textInputLayout.setBoxStrokeColor(Color.RED);

                }
                else if(!isValidNumber(contact1)){
                    submitbtn.setVisibility(View.GONE);
                    stud_contact1_textInputLayout.setHelperText("* Enter a valid 10 digit Indian phone number !");
                    stud_contact1_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_contact1_textInputLayout.setHelperText("");
                    stud_contact1_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String contact1 = contactno.getText().toString().trim();
                if(contact1.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_contact1_textInputLayout.setHelperText("* Required field");
                    stud_contact1_textInputLayout.setBoxStrokeColor(Color.RED);

                }
                else if(!isValidNumber(contact1)){
                    submitbtn.setVisibility(View.GONE);
                    stud_contact1_textInputLayout.setHelperText("* Enter a valid 10 digit Indian phone number !");
                    stud_contact1_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_contact1_textInputLayout.setHelperText("");
                    stud_contact1_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }

            }
        });


        //contact2 validation
        contactno2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String contact2 = contactno2.getText().toString().trim();
                if(contact2.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_contact2_textInputLayout.setHelperText("* Required field");
                    stud_contact2_textInputLayout.setBoxStrokeColor(Color.RED);

                }
                else if(!isValidNumber(contact2)){
                    submitbtn.setVisibility(View.GONE);
                    stud_contact2_textInputLayout.setHelperText("* Enter a valid 10 digit Indian phone number !");
                    stud_contact2_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_contact2_textInputLayout.setHelperText("");
                    stud_contact2_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String contact2 = contactno2.getText().toString().trim();
                if(contact2.isEmpty()){
                    submitbtn.setVisibility(View.GONE);
                    stud_contact2_textInputLayout.setHelperText("* Required field");
                    stud_contact2_textInputLayout.setBoxStrokeColor(Color.RED);

                }
                else if(!isValidNumber(contact2)){
                    submitbtn.setVisibility(View.GONE);
                    stud_contact2_textInputLayout.setHelperText("* Enter a valid 10 digit Indian phone number !");
                    stud_contact2_textInputLayout.setBoxStrokeColor(Color.RED);
                }
                else{
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_contact2_textInputLayout.setHelperText("");
                    stud_contact2_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
            }
        });


        //address validation
        address.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usradd = address.getText().toString().trim();
                if(!usradd.isEmpty()){
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_add_textInputLayout.setHelperText("");
                    stud_add_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                else{
                    submitbtn.setVisibility(View.GONE);
                    stud_add_textInputLayout.setHelperText("* Required field");
                    stud_add_textInputLayout.setBoxStrokeColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usradd = address.getText().toString().trim();
                if(!usradd.isEmpty()){
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_add_textInputLayout.setHelperText("");
                    stud_add_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                else{
                    submitbtn.setVisibility(View.GONE);
                    stud_add_textInputLayout.setHelperText("* Required field");
                    stud_add_textInputLayout.setBoxStrokeColor(Color.RED);
                }
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String usrname= username.getText().toString().trim();
                if(!usrname.isEmpty()){
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_usrname_textInputLayout.setHelperText("");
                    stud_usrname_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                else{
                    submitbtn.setVisibility(View.GONE);
                    stud_usrname_textInputLayout.setHelperText("* Required field");
                    stud_usrname_textInputLayout.setBoxStrokeColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String usrname= username.getText().toString().trim();
                if(!usrname.isEmpty()){
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_usrname_textInputLayout.setHelperText("");
                    stud_usrname_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                else{
                    submitbtn.setVisibility(View.GONE);
                    stud_usrname_textInputLayout.setHelperText("* Required field");
                    stud_usrname_textInputLayout.setBoxStrokeColor(Color.RED);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pswd = password.getText().toString().trim();
                if(!pswd.isEmpty()){
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_pswd_textInputLayout.setHelperText("");
                    stud_pswd_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                else{
                    submitbtn.setVisibility(View.GONE);
                    stud_pswd_textInputLayout.setHelperText("* Required field");
                    stud_pswd_textInputLayout.setBoxStrokeColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pswd = password.getText().toString().trim();
                if(!pswd.isEmpty()){
                    submitbtn.setVisibility(View.VISIBLE);
                    stud_pswd_textInputLayout.setHelperText("");
                    stud_pswd_textInputLayout.setBoxStrokeColor(Color.rgb(101, 31, 255));
                }
                else{
                    submitbtn.setVisibility(View.GONE);
                    stud_pswd_textInputLayout.setHelperText("* Required field");
                    stud_pswd_textInputLayout.setBoxStrokeColor(Color.RED);
                }
            }
        });












    }




    //upload image to firebase and details of student
    private void uploadPhotoToFirebase(Uri uri){
        mauth = FirebaseAuth.getInstance();
        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        mauth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //model class
                    String name1 = name.getText().toString().trim();
                    String email1 = email.getText().toString().trim();
                    String dob1 = dob.getText().toString().trim();
                    String contact1 = contactno.getText().toString().trim();
                    String contact2 = contactno2.getText().toString().trim();
                    String grade1 = grade.getText().toString().trim();
                    String address1 = address.getText().toString().trim();
                    String username1 = username.getText().toString().trim();
                    String password1 = password.getText().toString().trim();

                    StorageReference fileRef = reference.child(uid+"."+getFilesExtention(uri));
                    fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    StudentInserter studentImage = new StudentInserter(uri.toString(),name1,email1,dob1,contact1,contact2,grade1,address1,username1,password1,uid);
                                    FirebaseDatabase.getInstance().getReference("Student Details").child(uid).setValue(studentImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Add_Student.this,"Details Uploaded Successfully !!",Toast.LENGTH_LONG).show();
                                                name.setText("");
                                                email.setText("");
                                                dob.setText("");
                                                contactno.setText("");
                                                contactno2.setText("");
                                                grade.setText("");
                                                address.setText("");
                                                username.setText("");
                                                password.setText("");
                                                name.setEnabled(true);
                                                email.setEnabled(true);
                                                dob.setEnabled(true);
                                                contactno.setEnabled(true);
                                                contactno2.setEnabled(true);
                                                grade.setEnabled(true);
                                                address.setEnabled(true);
                                                username.setEnabled(true);
                                                password.setEnabled(true);
                                                profilepic.setImageResource(R.drawable.ic_addphoto_foreground);
                                                text.setText("Add Photo");
                                                Toast.makeText(Add_Student.this,"User registered successfully !",Toast.LENGTH_LONG).show();
                                                progressupload.setVisibility(View.INVISIBLE);
                                            }
                                            else{
                                                Toast.makeText(Add_Student.this,"User registration unsuccessful !",Toast.LENGTH_LONG).show();
                                                progressupload.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });

                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            name.setEnabled(false);
                            email.setEnabled(false);
                            dob.setEnabled(false);
                            contactno.setEnabled(false);
                            contactno2.setEnabled(false);
                            grade.setEnabled(false);
                            address.setEnabled(false);
                            username.setEnabled(false);
                            password.setEnabled(false);
                            progressupload.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Add_Student.this,"Image Upload Failed !!",Toast.LENGTH_LONG).show();
                        }
                    });

                    Snackbar.make(findViewById(R.id.studentaddlayout),"User has now been authenticated",Snackbar.LENGTH_LONG).show();

                }
                else{
                    Snackbar.make(findViewById(R.id.studentaddlayout),"Couldn't authenticate user as email and password entered is already used by another user!",Snackbar.LENGTH_LONG).show();
                }
            }
        });


    }

    private String getFilesExtention(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    //validation methods
    public static boolean isWord(String in){
        return Pattern.matches("^[a-zA-Z\\s]*$",in);
    }

    public boolean isEmail(String in){
        String email_Regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(email_Regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(in);
        return matcher.find();
    }

    public static boolean isValidNumber(String s)
    {
        Pattern p = Pattern.compile("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }


}