package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Admin_Check_Payment_History extends AppCompatActivity {

    DatabaseReference DataRef =   FirebaseDatabase.getInstance().getReference("Recieved Payment Details");
    RecyclerView recyclerView;
    EditText searchname;
    Adapter adapter;
    ArrayList<FeeDetails> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Check Recieved Payments");
        setContentView(R.layout.activity_admin_check_payment_history);
        recyclerView = findViewById(R.id.recyclerView3);
        searchname = findViewById(R.id.searchPayment);
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
        adapter = new Adapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                list.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    FeeDetails model = snapshot.getValue(FeeDetails.class);
                    //FeeDetails model = datasnapshot.getValue(FeeDetails.class);
                    list.add(model);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchname.addTextChangedListener(new TextWatcher() {
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
        ArrayList<FeeDetails> filterlist = new ArrayList<>();
        for(FeeDetails item : list){
            if(item.getStudent_name().toLowerCase().contains(text.toLowerCase())){
                filterlist.add(item);
            }
        }
        adapter.Filteredlist(filterlist);
    }
}