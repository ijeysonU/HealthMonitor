package com.example.healthmonitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cInfoDisp {
    private String crr;
    private String ndis;

    public String getNdis() {
        return ndis;
    }

    public void setNdis(String ndis) {
        this.ndis = ndis;
    }

    public cInfoDisp(JSONObject a){
        try {
            ndis = a.getString("ndisp").toString() ;
        } catch (JSONException e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static ArrayList<cInfoDisp> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<cInfoDisp> dispositivos = new ArrayList<>();
        for (int i = 0; i < datos.length() ; i++)
        {
            dispositivos.add(new cInfoDisp(datos.getJSONObject(i)));
        }
        return dispositivos;
    }
}
