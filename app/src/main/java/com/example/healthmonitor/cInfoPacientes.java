package com.example.healthmonitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cInfoPacientes {
    private String PNac;
    public String getPNac() {
        return PNac;
    }

    public void setPNac(String PNac) {
        this.PNac = PNac;
    }


    public cInfoPacientes(JSONObject a){
        try {
            PNac = a.getString("nactivos").toString() ;
        } catch (JSONException e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static ArrayList<cInfoPacientes> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<cInfoPacientes> dispositivos = new ArrayList<>();
        for (int i = 0; i < datos.length() ; i++)
        {
            dispositivos.add(new cInfoPacientes(datos.getJSONObject(i)));
        }
        return dispositivos;
    }
}
