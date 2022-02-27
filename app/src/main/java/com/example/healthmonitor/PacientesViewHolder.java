package com.example.healthmonitor;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PacientesViewHolder extends RecyclerView.ViewHolder {
    TextView tViewPaciente, tViewIdent, tViewCondiEdad, tViewReg;
    public PacientesViewHolder(@NonNull View itemView) {
        super(itemView);
        tViewPaciente = itemView.findViewById(R.id.txtPaciente);
        tViewIdent = itemView.findViewById(R.id.txtidentificacion);
        tViewCondiEdad = itemView.findViewById(R.id.txtCondicionEdad);
        tViewReg = itemView.findViewById(R.id.txtUltimoRegistro);
    }
}
