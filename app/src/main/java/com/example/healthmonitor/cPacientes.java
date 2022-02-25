package com.example.healthmonitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class cPacientes {

    private String identificacion;
    private String nombres;
    private String apellidos;
    private String condicion;
    public cPacientes(JSONArray a)  { //String nom, String are, String ide, String img1, String img2
        try {
            JSONObject json = new JSONObject(a.get(0).toString());
            nombre = a.getString("nombres").toString() ;
            area = a.getString("area").toString() ;
            idEv = a.getString("idevaluador").toString() ;
            urlImg1 = a.getString("imgJPG").toString() ;
            urlImg2 = a.getString("imgjpg").toString() ;
        } catch (JSONException e) {
            System.out.println("Error: " + e.toString());
        }
    }
}
