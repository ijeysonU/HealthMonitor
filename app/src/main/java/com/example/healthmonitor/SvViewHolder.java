package com.example.healthmonitor;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SvViewHolder extends RecyclerView.ViewHolder{
    TextView tViewFc, tViewSo, tViewTm, tViewFr;
    public SvViewHolder(@NonNull View itemView) {
        super(itemView);
        tViewFc = itemView.findViewById(R.id.txtFrecuencia);
        tViewSo = itemView.findViewById(R.id.txtSaturacion);
        tViewTm = itemView.findViewById(R.id.txtTemperatura);
        tViewFr = itemView.findViewById(R.id.txtFechaRegistro);
    }
}
