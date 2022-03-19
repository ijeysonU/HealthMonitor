package com.example.healthmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ws.statics;

public class MainMenu2 extends AppCompatActivity {
    //statics st = new statics();
    DrawerLayout drawerLayout;
    ArrayList<cResumen> cRes;
    private RequestQueue rq;
    RecyclerView res;
    RecyclerView inf1;
    RecyclerView inf2;
    String URL = "";
    String user;
    String valorUser;
    String Rol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_menu2);
        rq = Volley.newRequestQueue(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        Bundle bundle = this.getIntent().getExtras();
        valorUser = bundle.getString("CorreoUser");
        Rol = bundle.getString("Rol");
        user = "us="+bundle.getString("CorreoUser");

        findViewById(R.id.imageOpenMenu).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //se le asigna la acción de abrir el menú
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
        );
        // si por alguna razón no hay datos de usuario, regresar a la interfaz de login
        if (valorUser == null) {
            //onBackPressed();
            Intent intent = new Intent(MainMenu2.this, MainActivity.class);
            startActivity(intent);
        }
        //obtiene el navigation view en donde estará ubicado elementos como
        NavigationView navigationView = findViewById(R.id.id_opctions_menu);
        navigationView.setItemIconTintList(null);

        //establece el frame utilizado para la presentación de las ventanas
        NavController navController = Navigation.findNavController(MainMenu2.this, R.id.content_frame);
        //asigna la vista de navegación al controlador
        NavigationUI.setupWithNavController(navigationView, navController);

        //obtenemos el encabezado del menú
        View headMenu = navigationView.getHeaderView(0);
        //modifica el encabezado
        changeHeaderView(headMenu);
        Menu bodyMenu = navigationView.getMenu();

        //MyLogs.info("items: " + bodyMenu.size());


        //configureMenu(bodyMenu);

        dinamicMenu(bodyMenu);
        //jsonObjectRequest(valorUser);
            if (statics.rol.equals("2")){
                loadFragment(new paciente_paciente());
            }else{
                loadFragment(new home_fragment());
            }


    }

    private void changeHeaderView(View headMenu) {
        //se modifica el label, estableciendo el nombre del usuario logeado.

        String userName = statics.usuario;
        userName = userName.length() > 30 ? userName.substring(0, 25) + "..." : userName;
        ((TextView)headMenu.findViewById(R.id.txtTipoUser)).setText(statics.tusuario);
        ((TextView) headMenu.findViewById(R.id.profile_username)).setText(userName);
        //de igual manera, se modifica la imagen por la foto del usuario
        ImageView userImage = headMenu.findViewById(R.id.profile_image);
        Glide.with(headMenu)
                .load(R.drawable.user)
                .error(R.drawable.user)
                .into(userImage);
    }

    private void jsonObjectRequest(String dt){
        URL = statics.urlGeneral+"wResumen?"+dt;
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

                            cRes = cResumen.JsonObjectsBuild(jsonArray);
                            //adaptador_evaluadores adaptador_evaluadores = new adaptador_evaluadores(MainActivity.this, lstEval);
                            adaptador_resumen adaptador_resumen = new adaptador_resumen(MainMenu2.this, cRes);
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


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void dinamicMenu(Menu menu) {

        String rol = Rol;

        List<MenuItem> menus = new ArrayList<MenuItem>();

        MenuItem portal = menu.add("Home");
        portal.setIcon(R.drawable.ic_baseline_home_24);
        portal.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (statics.rol.equals("2")){
                    loadFragment(new paciente_paciente());
                }else{
                    loadFragment(new home_fragment());
                }

                return true;
            }
        });

        MenuItem profile = menu.add("User profile");
        profile.setIcon(R.drawable.ic_baseline_person_24);
        profile.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                loadFragment(new PerfilUsuario());
                return true;
            }
        });

        MenuItem tmp;
        if (rol.equals("3") || rol.equals("4")) {
            SubMenu navigation_root = menu.addSubMenu("Patients");
            tmp = navigation_root.add("My patients")
                    .setIcon(R.drawable.ic_baseline_airline_seat_recline_extra_24);

            tmp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    loadFragment(new pacientes());

                    return true;
                }
            });
        }
        if (rol.matches("[4]")) {
            SubMenu navigation_admin = menu.addSubMenu("Devices");
            tmp = navigation_admin.add("My Devices").setIcon(R.drawable.ic_baseline_settings_cell_24);
            menus.add(tmp);
            tmp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    loadFragment(new conectarbt());
                    return false;
                }
            });
            tmp = navigation_admin.add("Reporting Hours").setIcon(R.drawable.ic_baseline_calendar_today_24);
            menus.add(tmp);
        }


        SubMenu navigation_others = menu.addSubMenu("Others");

        tmp = navigation_others.add("Sign out");
        tmp.setIcon(R.drawable.ic_baseline_exit_to_app_24);
        tmp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                statics.usuario = null;
                onBackPressed();
                return false;
            }
        });

        for (MenuItem temp : menus) {
            temp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    return true;
                }
            });
        }
    }
}