package com.example.tution_management_system_app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView noticeTitle;
    View v;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.notice_image_single_view);
        noticeTitle = itemView.findViewById(R.id.notice_titletxt);
        v = itemView;
    }
}
