package com.example.healthmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adaptadorResPacientes extends  RecyclerView.Adapter<ResPacViewHolder> {
    private Context Ctx;
    private List<cInfoPacientes> lstPac;
    @NonNull
    @Override
    public ResPacViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(Ctx);
        View view = inflater.inflate(R.layout.lyinfo2, null);
        return new ResPacViewHolder(view);
    }

    public adaptadorResPacientes(Context mCtx, List<cInfoPacientes> dispositivo) {
        this.lstPac = dispositivo;
        Ctx=mCtx;
    }

    @Override
    public void onBindViewHolder(@NonNull ResPacViewHolder holder, int position) {
        cInfoPacientes disp = lstPac.get(position);
        holder.NPac.setText(disp.getPNac());
    }

    @Override
    public int getItemCount() {
        final int size = lstPac.size();
        return size;
    }
}
