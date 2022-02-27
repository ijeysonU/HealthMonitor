package com.example.healthmonitor;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ResDisViewHolder extends RecyclerView.ViewHolder{
    TextView NDis;
    public ResDisViewHolder(View itemView) {
        super(itemView);

        NDis= itemView.findViewById(R.id.txtDispositivos);

        //textideEvaluador = itemView.findViewById(R.id.txtidentificacion);
        //imageView = itemView.findViewById(R.id.imgAvatar);
    }

}
