package com.example.healthmonitor;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ws.statics;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link paciente_paciente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class paciente_paciente extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final long EXECUTION_TIME = 6000;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    Thread hilo;
    private Handler handler;
    private RequestQueue rq;
    String URL;
    public paciente_paciente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment paciente_paciente.
     */
    // TODO: Rename and change types and number of parameters
    public static paciente_paciente newInstance(String param1, String param2) {
        paciente_paciente fragment = new paciente_paciente();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_paciente_paciente, container, false);
        rq = Volley.newRequestQueue(getContext());
        jsonObjectRequestRes();
        handleSSLHandshake();
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                jsonObjectRequestRes();
                handler.postDelayed(this, EXECUTION_TIME);
            }
        }, EXECUTION_TIME);
        return v;
    }

    private void jsonObjectRequestRes(){
        URL = statics.urlGeneral+"wUltimoRegistro?us="+statics.cusuario;
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            int resId = R.anim.layout_animation_down_to_up;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(),
                                    resId);
                            int x = jsonArray.length();
                            for (int i = 0; i < x; i++ ) {
                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                                String dataT = jsonObject.getString("fc");
                                String dataB = jsonObject.getString("spo");
                                String dataS = jsonObject.getString("tmp");
                                String dataD = jsonObject.getString("fa");
                                TextView nm = (TextView)v.findViewById(R.id.txtPacienteN);
                                nm.setText(statics.usuario);
                                TextView con = (TextView)v.findViewById(R.id.txtCondicion);
                                con.setText(statics.tusuario);
                                TextView cr = (TextView)v.findViewById(R.id.txtCorreoPac);
                                cr.setText(statics.cusuario);
                                TextView bpm = (TextView)v.findViewById(R.id.txtBMP);
                                bpm.setText(dataT);
                                TextView spo = (TextView)v.findViewById(R.id.txtSPO);
                                spo.setText(dataB);
                                TextView tmp = (TextView)v.findViewById(R.id.txtTEMP);
                                tmp.setText(dataS);
                                TextView fch = (TextView)v.findViewById(R.id.txtRegistroDate);
                                fch.setText(dataD);
                            }

                        }catch (JSONException ex){
                            System.out.println("Error: "+ex.toString());
                            //Toast.makeText(ex.getMessage(),Toast.LENGTH_LONG);
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