package com.example.healthmonitor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

import ws.statics;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link registros_pacientes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class registros_pacientes extends Fragment {
    View view;
    RecyclerView res;
    ArrayList<cSignosVitales> cSv;
    private RequestQueue rq;
    String URL = "";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public registros_pacientes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment registros_pacientes.
     */
    // TODO: Rename and change types and number of parameters
    public static registros_pacientes newInstance(String param1, String param2) {
        registros_pacientes fragment = new registros_pacientes();
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
        view = inflater.inflate(R.layout.fragment_registros_pacientes, container, false);
        res = view.findViewById(R.id.rcvDatos);
        rq = Volley.newRequestQueue(getContext());
        jsonObjectRequestRes();
        res.setHasFixedSize(true);
        res.setLayoutManager(new LinearLayoutManager(getContext()));
        res.setItemAnimator(new DefaultItemAnimator());
        TextView pc = view.findViewById(R.id.txtPaciente);
        pc.setText(statics.npaciente);
        return view;
    }

    private void jsonObjectRequestRes(){


        URL = statics.urlGeneral+"wListarRegistrosPac?us="+statics.cusuario+"&iPac="+statics.paciente;
        System.out.println(URL);
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
                            res.setLayoutAnimation(animation);
                            cSv = cSignosVitales.JsonObjectsBuild(jsonArray);

                            adaptadorSV adaptador_sv= new adaptadorSV(getContext(), cSv);
                            res.setAdapter(adaptador_sv);

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
}