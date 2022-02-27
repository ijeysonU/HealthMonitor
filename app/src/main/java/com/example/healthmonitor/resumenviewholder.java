package com.example.healthmonitor;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class resumenviewholder extends RecyclerView.ViewHolder{
    TextView Npac, Nemr;
    public resumenviewholder(View itemView) {
        super(itemView);

        Npac= itemView.findViewById(R.id.txtNumeroPacientes);
        Nemr = itemView.findViewById(R.id.txtNumeroAlertas);

        //textideEvaluador = itemView.findViewById(R.id.txtidentificacion);
        //imageView = itemView.findViewById(R.id.imgAvatar);
    }

}
