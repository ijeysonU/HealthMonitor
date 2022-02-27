package com.example.healthmonitor;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResPacViewHolder extends RecyclerView.ViewHolder{
    TextView NPac;
    public ResPacViewHolder(@NonNull View itemView) {
        super(itemView);
        NPac= itemView.findViewById(R.id.txtNumeroPacientes);
    }
}
