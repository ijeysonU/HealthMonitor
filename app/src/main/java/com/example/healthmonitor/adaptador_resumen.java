package com.example.healthmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class adaptador_resumen extends  RecyclerView.Adapter<resumenviewholder> {
    private Context Ctx;
    private List<cResumen> lstResumen;
    @NonNull
    @Override
    public resumenviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(Ctx);
        View view = inflater.inflate(R.layout.lyt_resumen1, null);
        return new resumenviewholder(view);
    }

    public adaptador_resumen(Context mCtx, List<cResumen> usuarios) {
        this.lstResumen = usuarios;
        Ctx=mCtx;
    }

    @Override
    public void onBindViewHolder(@NonNull resumenviewholder holder, int position) {
        cResumen resumen = lstResumen.get(position);
        holder.Npac.setText(resumen.getnPac());
        holder.Nemr.setText(resumen.getnEmg());

    }

    @Override
    public int getItemCount() {
        final int size = lstResumen.size();
        return size;
    }
}
