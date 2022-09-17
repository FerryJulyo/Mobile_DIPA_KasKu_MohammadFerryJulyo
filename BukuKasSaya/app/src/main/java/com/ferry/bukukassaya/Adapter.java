package com.ferry.bukukassaya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.CashFlowViewHolder> {
    private Context context;
    private ArrayList tanggal, nominal, keterangan, tipe;

    public Adapter(Context context, ArrayList tanggal, ArrayList nominal, ArrayList keterangan, ArrayList tipe) {
        this.context = context;
        this.tanggal = tanggal;
        this.nominal = nominal;
        this.keterangan = keterangan;
        this.tipe = tipe;
    }

    @NonNull
    @Override
    public CashFlowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cashflow, parent, false);
        return new CashFlowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CashFlowViewHolder holder, int position) {
        holder.keterangan.setText(String.valueOf(keterangan.get(position)));
        holder.tanggal.setText(String.valueOf(tanggal.get(position)));
        if (tipe.get(position).equals("masuk")){
            holder.nominal.setText(String.format("[+] Rp.%s", nominal.get(position)));
            holder.nominal.setTextColor(ContextCompat.getColor(context, R.color.green));
            holder.tipe.setImageResource(R.drawable.uang_masuk);
        }else{
            holder.nominal.setText(String.format("[-] Rp.%s", nominal.get(position)));
            holder.nominal.setTextColor(ContextCompat.getColor(context, R.color.redDrop));
            holder.tipe.setImageResource(R.drawable.uang_keluar);
        }
    }

    @Override
    public int getItemCount() {
        return tanggal.size();
    }

    public class CashFlowViewHolder extends RecyclerView.ViewHolder {
        TextView tanggal, nominal, keterangan;
        ImageView tipe;
        public CashFlowViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggal     = itemView.findViewById(R.id.tanggal);
            nominal     = itemView.findViewById(R.id.nominal);
            keterangan  = itemView.findViewById(R.id.keterangan);
            tipe        = itemView.findViewById(R.id.gambar);
        }
    }
}
