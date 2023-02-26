package com.example.tution_management_system_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class StudentNotice extends AppCompatActivity {
    EditText inputSearch;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Notice> options;
    FirebaseRecyclerAdapter<Notice,MyViewHolder2> adapter;
    DatabaseReference DataRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_student_notice);
        DataRef = FirebaseDatabase.getInstance().getReference().child("Notices");
        inputSearch = findViewById(R.id.inputSearch1);
        recyclerView = findViewById(R.id.noticerecyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        LoadData("");
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString()!=null){
                    LoadData(editable.toString());
                }
                else{
                    LoadData("");
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void LoadData(String data){
        Query query = DataRef.orderByChild("notice_title").startAt(data).endAt(data+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Notice>().setQuery(query,Notice.class).build();
        adapter = new FirebaseRecyclerAdapter<Notice, MyViewHolder2>(options){
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder2 holder, int position, @NonNull final Notice model) {
                holder.noticeTitle.setText(model.getNotice_title());
                String notice_id = model.getNotice_id();
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StudentNotice.this,StudentViewNotice.class);
                        intent.putExtra("NoticeKey",getRef(holder.getBindingAdapterPosition()).getKey() );
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_notice,parent,false);
                return new MyViewHolder2(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


}