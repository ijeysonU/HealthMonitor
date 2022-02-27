package com.example.healthmonitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class cSignosVitales {
    private String frec;
    private String oxig;
    private String temp;
    private String fReg;

    public cSignosVitales(JSONObject a)  {
        try {
            frec = a.getString("fcard").toString() ;
            oxig = a.getString("sox").toString() ;
            temp = a.getString("tmp").toString() ;
            fReg = a.getString("freg").toString();


        } catch (JSONException e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static ArrayList<cSignosVitales> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<cSignosVitales> svitales = new ArrayList<>();

        for (int i = 0; i < datos.length() ; i++) {
            svitales.add(new cSignosVitales(datos.getJSONObject(i)));
        }
        return svitales;
    }

    public String getFrec() {
        return frec;
    }

    public void setFrec(String frec) {
        this.frec = frec;
    }

    public String getOxig() {
        return oxig;
    }

    public void setOxig(String oxig) {
        this.oxig = oxig;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getfReg() {
        return fReg;
    }

    public void setfReg(String fReg) {
        this.fReg = fReg;
    }
}
