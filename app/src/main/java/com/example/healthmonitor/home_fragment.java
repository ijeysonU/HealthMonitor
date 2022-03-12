package com.example.healthmonitor;

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
 * Use the {@link home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView res;
    RecyclerView inf1;
    RecyclerView inf2;
    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<cResumen> cRes;
    ArrayList<cInfoDisp> cInfo1;
    ArrayList<cInfoPacientes> cInfo2;
    private RequestQueue rq;
    String URL = "";
    String user;
    String valorUser;
    String Rol;

    public home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static home_fragment newInstance(String param1, String param2) {
        home_fragment fragment = new home_fragment();
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
        view = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        res = view.findViewById(R.id.recyclerView);
        inf1 = view.findViewById(R.id.rcvdata1);
        inf2 = view.findViewById(R.id.rcvdata2);
        rq = Volley.newRequestQueue(getContext());
        rq = Volley.newRequestQueue(getContext());
        rq = Volley.newRequestQueue(getContext());
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        String dt = "us="+statics.cusuario;
        jsonObjectRequestRes(dt);
        jsonObjectRequesti1(dt);
        jsonObjectRequesti2(dt);
        res.setHasFixedSize(true);
        res.setLayoutManager(new LinearLayoutManager(getContext()));
        res.setItemAnimator(new DefaultItemAnimator());

        inf1.setHasFixedSize(true);
        inf1.setLayoutManager(new LinearLayoutManager(getContext()));
        inf1.setItemAnimator(new DefaultItemAnimator());

        inf2.setHasFixedSize(true);
        inf2.setLayoutManager(new LinearLayoutManager(getContext()));
        inf2.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    private void jsonObjectRequestRes(String dt){
        URL = statics.urlGeneral+"wResumen?us="+statics.cusuario;
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
                            cRes = cResumen.JsonObjectsBuild(jsonArray);

                            adaptador_resumen adaptador_resumen = new adaptador_resumen(getContext(), cRes);
                            res.setAdapter(adaptador_resumen);

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

    private void jsonObjectRequesti1(String dt){
        URL = statics.urlGeneral+"wResumenDispositivos?us="+statics.cusuario;
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

                            inf1.setLayoutAnimation(animation);
                            cInfo1 = cInfoDisp.JsonObjectsBuild(jsonArray);

                            adaptadorResDispositivos adaptador_infdis = new adaptadorResDispositivos(getContext(), cInfo1);
                            inf1.setAdapter(adaptador_infdis);

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

    private void jsonObjectRequesti2(String dt){
        URL = statics.urlGeneral+"wResumenPacientes?us="+statics.cusuario;
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

                            inf2.setLayoutAnimation(animation);
                            cInfo2 = cInfoPacientes.JsonObjectsBuild(jsonArray);

                            adaptadorResPacientes adaptador_infPac = new adaptadorResPacientes(getContext(), cInfo2);
                            inf2.setAdapter(adaptador_infPac);

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