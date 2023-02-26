package com.example.tution_management_system_app.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tution_management_system_app.AttendanceDetails;
import com.example.tution_management_system_app.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList<AttendanceDetails> arrayList;
    Context context;

    public Adapter(ArrayList<AttendanceDetails> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceDetails model = arrayList.get(position);
        holder.name.setText(model.getStudent_name());
        holder.date.setText(model.getDate());
        holder.status.setText(model.getStatus());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void Filteredlist(ArrayList<AttendanceDetails> filterlist) {
        arrayList = filterlist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,date,status;
        View v;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.attend_studname);
            date = itemView.findViewById(R.id.attend_date);
            status = itemView.findViewById(R.id.attend_status);
        }
    }
}
