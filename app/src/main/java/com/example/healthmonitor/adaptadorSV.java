package com.example.healthmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adaptadorSV extends RecyclerView.Adapter<SvViewHolder>{
    private Context Ctx;
    private List<cSignosVitales> lstSV;

    public adaptadorSV(Context mCtx, List<cSignosVitales> svitales) {
        this.lstSV = svitales;
        Ctx=mCtx;
    }

    @NonNull
    @Override
    public SvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(Ctx);
        View view = inflater.inflate(R.layout.lytdata_paciente, null);
        return new SvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SvViewHolder holder, int position) {
        cSignosVitales sVitales = lstSV.get(position);
        holder.tViewFr.setText(sVitales.getFrec());
        holder.tViewSo.setText(sVitales.getOxig());
        holder.tViewTm.setText(sVitales.getTemp());
        holder.tViewFr.setText(sVitales.getfReg());
    }

    @Override
    public int getItemCount() {
        final int size = lstSV.size();
        return size;
    }
}
