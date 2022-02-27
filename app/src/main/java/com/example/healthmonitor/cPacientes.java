package com.example.healthmonitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cPacientes {

    private String identificacion;
    private String paciente;
    private String condicion;
    private String edad;
    private String lastReg;
    public cPacientes(JSONObject a)  { //String nom, String are, String ide, String img1, String img2
        try {
            identificacion = a.getString("idnt").toString() ;
            paciente = a.getString("paciente").toString() ;
            condicion = a.getString("condicio").toString() ;
            lastReg = a.getString("fecac").toString();
            edad = a.getString("det").toString();


        } catch (JSONException e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static ArrayList<cPacientes> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<cPacientes> pacientes = new ArrayList<>();

        for (int i = 0; i < datos.length() ; i++) {
            pacientes.add(new cPacientes(datos.getJSONObject(i)));
        }
        return pacientes;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getLastReg() {
        return lastReg;
    }

    public void setLastReg(String lastReg) {
        this.lastReg = lastReg;
    }
}
