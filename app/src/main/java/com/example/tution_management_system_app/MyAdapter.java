package com.example.tution_management_system_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends FirebaseRecyclerAdapter<StudentInserter,MyAdapter.myviewholder>{

    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    public MyAdapter(@NonNull FirebaseRecyclerOptions<StudentInserter> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder,final int position, @NonNull final StudentInserter model) {
        //On card display
        holder.name.setText(model.getName());
        holder.grade.setText(model.getGrade());
        holder.email.setText(model.getEmail());
        Glide.with(holder.img.getContext()).load(model.getImageUrl()).into(holder.img);


        holder.edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext()).setContentHolder(new ViewHolder(R.layout.dialogcontent)).setExpanded(true,1100).create();

                View myview = dialogPlus.getHolderView();
                CircleImageView purl = myview.findViewById(R.id.profilepic);
                TextInputEditText uname = myview.findViewById(R.id.uname);
                TextInputEditText uemail = myview.findViewById(R.id.uemail);
                TextInputEditText udob = myview.findViewById(R.id.udob);
                TextInputEditText ucontact = myview.findViewById(R.id.ucontactno);
                TextInputEditText ucontact2 = myview.findViewById(R.id.ucontactno2);
                AutoCompleteTextView ugrade = myview.findViewById(R.id.ugrade);
                String[] items = {"Jr.Kg","Sr.Kg","1st","2nd","3rd","4th","5th","6th","7th","8th"};
                ArrayAdapter<String> adapterItems;
                adapterItems = new ArrayAdapter<String>(myview.getContext(), R.layout.list_item,items);

                TextInputEditText uaddress = myview.findViewById(R.id.uaddress);
                TextInputEditText uusername = myview.findViewById(R.id.uusername);
                TextInputEditText upassword = myview.findViewById(R.id.upassword);
                Button usubmit = myview.findViewById(R.id.usubmit);
                String imagelink = model.getImageUrl();
                Picasso.get().load(imagelink).into(purl);
                uname.setText(model.getName());
                uemail.setText(model.getEmail());
                udob.setText(model.getDob());
                ucontact.setText(model.getContact_no());
                ucontact2.setText(model.getContact_no_2());
                ugrade.setText(model.getGrade());

                ugrade.setAdapter(adapterItems);

                ugrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
                    }
                });


                uaddress.setText(model.getAddress());
                uusername.setText(model.getUsername());
                upassword.setText(model.getPassword());


                //date picker for d.o.b
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
                        String Format = "dd/MM/yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
                        udob.setText(sdf.format(calendar.getTime()));
                    }
                };

                udob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(myview.getContext(),date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });


                //click on image view to launch gallery
                purl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //setting profile pic on imageview
                        StorageReference reference = FirebaseStorage.getInstance().getReference();
                        //mGetContent.launch("image/*");
                    }
                });

                dialogPlus.show();



                usubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("name",uname.getText().toString().trim());
                        map.put("email",uemail.getText().toString().trim());
                        map.put("contact_no",ucontact.getText().toString().trim());
                        map.put("contact_no_2",ucontact2.getText().toString().trim());
                        map.put("dob",udob.getText().toString().trim());
                        map.put("grade",ugrade.getText().toString().trim());
                        map.put("address",uaddress.getText().toString().trim());
                        map.put("username",uusername.getText().toString().trim());
                        map.put("password",upassword.getText().toString().trim());


                        FirebaseDatabase.getInstance().getReference().child("Student Details")
                                .child(getRef(holder.getBindingAdapterPosition()).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(myview.getContext(),"Data Updated Successfully !",Toast.LENGTH_LONG).show();
                                       dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(myview.getContext(),"Data Updation Failure !",Toast.LENGTH_LONG).show();
                                        dialogPlus.dismiss();
                                    }
                                });

                    }


                });

            }
        });




        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(holder.img.getContext()).setTitle("Read Carefully !").setMessage(" Important : First, Please log out of your admin account and then log in with "+model.getName()+" account to unauthorize it and then log in back to your admin account to delete "+model.getName()+" personal details. If done, click 'yes' or else click on 'no' ").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        //checking if authorization is deleted first
                        mAuth.fetchSignInMethodsForEmail(model.getEmail()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.getResult().getSignInMethods().size() == 0){
                                    FirebaseStorage mStorage = FirebaseStorage.getInstance();
                                    String imagelink = model.getImageUrl();
                                    StorageReference imageref = mStorage.getReferenceFromUrl(imagelink);
                                    imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //for deleting user details as well
                                            Toast.makeText(view.getContext(), "User Profile Image  deleted successfully !",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    // email not existed
                                    FirebaseDatabase.getInstance().getReference().child("Student Details")
                                            .child(getRef(holder.getBindingAdapterPosition()).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(view.getContext(), "Details deleted !",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }else {
                                    // email existed
                                    Toast.makeText(view.getContext(), "Details cannot be deleted as user is still authenticated !",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });




                    }
                }).setNegativeButton("No",null).show();

            }
        });


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        CircleImageView img;
        ImageView edit,delete;
        TextView name,grade,email;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametext);
            grade = (TextView) itemView.findViewById(R.id.gradetext);
            email = (TextView) itemView.findViewById(R.id.emailtext);
            edit = (ImageView) itemView.findViewById(R.id.editicon);
            delete = (ImageView) itemView.findViewById(R.id.deleteicon);
        }
    }
}
