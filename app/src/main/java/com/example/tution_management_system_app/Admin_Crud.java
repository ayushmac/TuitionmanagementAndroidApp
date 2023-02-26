package com.example.tution_management_system_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Crud extends AppCompatActivity {
    RecyclerView recview;
    MyAdapter adapter;
    FloatingActionButton add_stud_btn;
    //internet lister object
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    //recyler view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crud);
        getSupportActionBar().setTitle("Manage Students");
        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        add_stud_btn = findViewById(R.id.floatingActionButton);
        add_stud_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Add_Student.class);
                startActivity(intent);
                FirebaseRecyclerOptions<StudentInserter> options =
                        new FirebaseRecyclerOptions.Builder<StudentInserter>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("Student Details"), StudentInserter.class)
                                .build();
                adapter = new MyAdapter(options);
                recview.setAdapter(adapter);
            }
        });

        FirebaseRecyclerOptions<StudentInserter> options =
                new FirebaseRecyclerOptions.Builder<StudentInserter>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Student Details"), StudentInserter.class)
                        .build();
        adapter = new MyAdapter(options);
        recview.setAdapter(adapter);


    }




    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
        recview.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
        adapter.stopListening();
    }

    //searching records functionality
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s){
        FirebaseRecyclerOptions<StudentInserter> options =
                new FirebaseRecyclerOptions.Builder<StudentInserter>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Student Details").orderByChild("name").startAt(s).endAt(s+"\uf8ff"), StudentInserter.class)
                        .build();

        adapter = new MyAdapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }
}