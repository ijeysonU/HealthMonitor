package com.example.healthmonitor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ws.statics;

public class MainActivity extends AppCompatActivity {

    private RequestQueue rq;
    String URL = "";
    //statics st = new statics();
    String validador = "";
    String us;
    String usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        rq = Volley.newRequestQueue(this);
    }

    public void login(View view){


        String data = "";
        TextInputLayout dd  = (TextInputLayout)  findViewById(R.id.txtEmail);
        us = "us="+dd.getEditText().getText();
        usuario = dd.getEditText().getText().toString();
        statics.usuario=(usuario);
        TextInputLayout pss = (TextInputLayout) findViewById(R.id.txtpassword);
        String ps = "ps="+pss.getEditText().getText();
        data = us+"&"+ps;
        if (dd.getEditText().length()<=0
                || pss.getEditText().length()<=0){
            Toast.makeText(MainActivity.this, "Incomplete credentials", Toast.LENGTH_SHORT).show();
        }else{
            handleSSLHandshake();
            jsonObjectRequest(data);
        }

    }
    public void signIn(View view){
        Intent intent = new Intent(MainActivity.this, RegistroPersona.class);
        startActivity(intent);
    }


    private void jsonObjectRequest(String dt){
        URL = statics.urlGeneral+"wLogin?"+dt;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            //JSONArray jsonArray = response.getJSONArray("valver");

                            int resId = R.anim.layout_animation_down_to_up;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(),
                                    resId);
                            JSONObject jo = new JSONObject(response.get(0).toString());
                            validador = jo.getString("valver");
                            String vuser = jo.getString("usr");
                            statics.rol=(validador);
                            statics.usuario=vuser;
                            statics.cusuario=usuario;
                            statics.tusuario=jo.getString("tusr");
                            System.out.println("Valor de tipo de usuario:"+validador);
                            if (validador.equals("404")){
                                Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_LONG).show();
                            }else{
                                if (validador=="2"){

                                }
                                Intent intent = new Intent(MainActivity.this, MainMenu2.class);
                                Bundle b = new Bundle();
                                b.putString("CorreoUser", vuser);
                                b.putString("Rol", validador);
                                intent.putExtras(b);
                                startActivity(intent);
                            }

                        }catch (JSONException ex){
                            System.out.println("Error: "+ex.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: "+error.toString());
            }
        }
        );

        rq.add(jsonArrayRequest);
    }
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}