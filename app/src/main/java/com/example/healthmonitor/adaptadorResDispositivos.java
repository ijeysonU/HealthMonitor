package com.example.healthmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adaptadorResDispositivos  extends  RecyclerView.Adapter<ResDisViewHolder>  {
    private Context Ctx;
    private List<cInfoDisp> lstDisp;
    @NonNull
    @Override
    public ResDisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(Ctx);
        View view = inflater.inflate(R.layout.lyinfo1, null);
        return new ResDisViewHolder(view);
    }

    public adaptadorResDispositivos(Context mCtx, List<cInfoDisp> dispositivo) {
        this.lstDisp = dispositivo;
        Ctx=mCtx;
    }

    @Override
    public void onBindViewHolder(@NonNull ResDisViewHolder holder, int position) {
        cInfoDisp disp = lstDisp.get(position);
        holder.NDis.setText(disp.getNdis());
    }

    @Override
    public int getItemCount() {
        final int size = lstDisp.size();
        return size;
    }
}
