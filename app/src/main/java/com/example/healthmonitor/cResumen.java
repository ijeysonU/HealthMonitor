package com.example.healthmonitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cResumen {
    private  String nPac;
    private  String nEmg;

    public String getnPac() {
        return nPac;
    }

    public void setnPac(String nPac) {
        this.nPac = nPac;
    }

    public String getnEmg() {
        return nEmg;
    }

    public void setnEmg(String nEmg) {
        this.nEmg = nEmg;
    }

    public cResumen(JSONObject a){
        try {
            nPac = a.getString("npac").toString() ;
            nEmg = a.getString("nemer").toString() ;
        } catch (JSONException e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static ArrayList<cResumen> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<cResumen> pacientes = new ArrayList<>();
        for (int i = 0; i < datos.length() ; i++) {
            pacientes.add(new cResumen(datos.getJSONObject(i)));
        }
        return pacientes;
    }
}
