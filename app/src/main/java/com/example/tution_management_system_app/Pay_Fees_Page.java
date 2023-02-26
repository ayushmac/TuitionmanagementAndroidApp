package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Pay_Fees_Page extends AppCompatActivity implements PaymentResultListener {
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    TextInputEditText amount,paydate,contactno;
    Button paybtn;
    TextView payhistory;
    private String userId;
    String emailtxt;
    String contacttxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pay_fees_page);

        databaseReference  = FirebaseDatabase.getInstance().getReference();
        amount = findViewById(R.id.amount);
        paydate = findViewById(R.id.pay_date);
        paybtn = findViewById(R.id.razorpaybtn);
        contactno = findViewById(R.id.studcontact);
        payhistory = findViewById(R.id.chkhistorytxtview);
        //date picker for d.o.b
         /*
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
                String Format = "dd-MM-yy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
                paydate.setText(sdf.format(calendar.getTime()));
            }
        };

        paydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Pay_Fees_Page.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });*/

        paydate.setEnabled(false);
        Calendar calendar = Calendar.getInstance();
        String Format = "dd-MM-yy";
        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
        paydate.setText(sdf.format(calendar.getTime()));

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePayment();
            }
        });

        payhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pay_Fees_Page.this,Check_Payment_History.class);
                startActivity(intent);
            }
        });
    }

    private void makePayment(){

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_VMIK4VJsQubra0");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.studentlogo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Student Details");
        userId = user.getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                StudentInserter suser = snapshot.getValue(StudentInserter.class);

                if(user != null){
                    String emailtxt = suser.getEmail();
                    String sid  = suser.getSid();
                    String nametxt = suser.getName();
                    String contact = suser.getContact_no();
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Fee Payment Details").child(userId);
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                JSONObject options = new JSONObject();
                                int amt = Integer.parseInt(amount.getText().toString().trim()) * 100;
                                String amtfinal = Integer.toString(amt);
                                options.put("name", "Mac Tutorials");
                                options.put("description", "Tution fees paid on "+paydate.getText().toString().trim()+" .");
                                options.put("image", "");
                                //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
                                options.put("theme.color", "#673AB7");
                                options.put("currency", "INR");
                                options.put("amount", amtfinal);//pass amount in currency subunits
                                options.put("prefill.email", emailtxt);
                                options.put("prefill.contact",contactno.getText().toString().trim());
                                JSONObject retryObj = new JSONObject();
                                retryObj.put("enabled", true);
                                retryObj.put("max_count", 4);
                                options.put("retry", retryObj);
                                checkout.open(activity, options);

                            } catch(Exception e) {
                                Toast.makeText(Pay_Fees_Page.this,"Error in starting razor checkout !",Toast.LENGTH_LONG).show();
                            }
                            //String dop = paydate.getText().toString().trim();
                            /*if(snapshot.hasChild(dop)){

                            }*/

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Pay_Fees_Page.this,"Error in starting razor checkout !!",Toast.LENGTH_LONG).show();
            }
        });



    }


    @Override
    public void onPaymentSuccess(String s) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Student Details");
        userId = user.getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                StudentInserter suser = snapshot.getValue(StudentInserter.class);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Fee Payment Details").child(userId);
                if(user != null){
                    String emailtxt = suser.getEmail();
                    String sid  = suser.getSid();
                    String nametxt = suser.getName();
                    String fid = reference.push().getKey();
                    Map<String,Object> map = new HashMap<>();
                    map.put("student_id",sid);
                    map.put("student_name",nametxt);
                    map.put("date_of_payment",paydate.getText().toString().trim());
                    map.put("amount_paid",amount.getText().toString().trim());
                    map.put("status","paid");
                    map.put("fee_id",fid);

                    reference.child(fid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Map<String,Object> map1 = new HashMap<>();
                            map1.put("student_id",sid);
                            map1.put("student_name",nametxt);
                            map1.put("date_of_payment",paydate.getText().toString().trim());
                            map1.put("amount_paid",amount.getText().toString().trim());
                            map1.put("status","paid");
                            map1.put("fee_id",fid);
                            FirebaseDatabase.getInstance().getReference("Recieved Payment Details").child(fid).setValue(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(Pay_Fees_Page.this,"Payment details added to recieved payments successfully!",Toast.LENGTH_LONG).show();
                                    //paydate.setText("");
                                    amount.setText("");
                                    contactno.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Pay_Fees_Page.this,"Payment details failed to add to recieved payments !",Toast.LENGTH_LONG).show();
                                }
                            });
                            Toast.makeText(Pay_Fees_Page.this,"Payment details added to database successfully !",Toast.LENGTH_LONG).show();
                            //paydate.setText("");
                            amount.setText("");
                            contactno.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Pay_Fees_Page.this,"Payment details failed to add in database !",Toast.LENGTH_LONG).show();
                        }
                    });
                    //fee table insert






                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Pay_Fees_Page.this,"Error in starting razor checkout !!",Toast.LENGTH_LONG).show();
            }
        });
        //progressbar

        Toast.makeText(Pay_Fees_Page.this,"Payment success !",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        startActivity(new Intent(Pay_Fees_Page.this,StudentMain.class));
        Toast.makeText(Pay_Fees_Page.this,"Payment failure !",Toast.LENGTH_LONG).show();
    }
}