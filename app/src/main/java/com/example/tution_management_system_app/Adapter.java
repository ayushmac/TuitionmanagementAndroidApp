package com.example.tution_management_system_app;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList<FeeDetails> arrayList;
    Context context;

    public Adapter(ArrayList<FeeDetails> arrayList,Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feehistory,parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeeDetails model = arrayList.get(position);
        holder.name.setText(model.getStudent_name());
        holder.amt_paid.setText(model.getAmount_paid());
        holder.paydate.setText(model.getDate_of_payment());
        holder.paystatus.setText(model.getStatus());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void Filteredlist(ArrayList<FeeDetails> filterlist) {
        arrayList = filterlist;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,amt_paid,paydate,paystatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.fee_studname);
            amt_paid = itemView.findViewById(R.id.fee_amtpaid);
            paydate = itemView.findViewById(R.id.fee_paydate);
            paystatus = itemView.findViewById(R.id.fee_paystatus);
        }
    }
}
