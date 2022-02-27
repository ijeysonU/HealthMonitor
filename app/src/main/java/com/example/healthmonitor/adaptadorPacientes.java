package com.example.healthmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adaptadorPacientes extends RecyclerView.Adapter<PacientesViewHolder>
implements View.OnClickListener
{
    private Context Ctx;
    private List<cPacientes> lstResumen;
    private View.OnClickListener Listener;

    @NonNull
    @Override
    public PacientesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(Ctx);
        View view = inflater.inflate(R.layout.lytpaciente, null);
        view.setOnClickListener(this);
        return new PacientesViewHolder(view);
    }
    public adaptadorPacientes(Context mCtx, List<cPacientes> usuarios) {
        this.lstResumen = usuarios;
        Ctx=mCtx;
    }
    @Override
    public void onBindViewHolder(@NonNull PacientesViewHolder holder, int position) {
        cPacientes resumen = lstResumen.get(position);
        holder.tViewPaciente.setText(resumen.getPaciente());
        holder.tViewIdent.setText(resumen.getIdentificacion());
        holder.tViewCondiEdad.setText(resumen.getCondicion()+", "+resumen.getEdad()+" years");
        holder.tViewReg.setText("Last registration: "+resumen.getLastReg());
    }

    @Override
    public int getItemCount() {
        final int size = lstResumen.size();
        return size;
    }

    @Override
    public void onClick(View v) {
        if (Listener != null){
            Listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.Listener =listener;
    }
}
