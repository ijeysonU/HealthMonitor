package com.example.healthmonitor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import ws.statics;

public class MainMenu extends AppCompatActivity {
    statics st = new statics();
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void dinamicMenu(Menu menu) {
        //obtiene el permiso del usuario, para asi determinar que opciones se van a generar y cuales no
        String rol = st.getRol();

        List<MenuItem> menus = new ArrayList<MenuItem>();
        //genera cada uno de los items para el menú
        MenuItem portal = menu.add("Inicio");
        portal.setIcon(R.drawable.ic_baseline_home_24);
        portal.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Alerts.MessageToast(MainMenu.this, "clic en portal");
                loadFragment(new home_fragment());

                return true;
            }
        });

        MenuItem profile = menu.add("Perfil de usuario");
        profile.setIcon(R.drawable.ic_baseline_person_24);
        profile.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Alerts.MessageToast(MainMenu.this, "Mi perfil");
                loadFragment(new pacientes());
                return true;
            }
        });

        MenuItem tmp;
        if (rol.equals("3") ||rol.equals("4")) {
            SubMenu navigation_root = menu.addSubMenu("Pacientes");
            tmp = navigation_root.add("Mis pacientes").setIcon(R.drawable.ic_baseline_airline_seat_recline_extra_24);
            menus.add(tmp);
        }
        if (rol.matches("[4]")) {
            SubMenu navigation_admin = menu.addSubMenu("Dispositivos");
            tmp = navigation_admin.add("Mis dispositivos").setIcon(R.drawable.ic_baseline_settings_cell_24);
            menus.add(tmp);
            tmp = navigation_admin.add("Horarios de informe de los dispositivos").setIcon(R.drawable.ic_baseline_calendar_today_24);
            menus.add(tmp);
        }
        //if (rol.matches("[RAU]")) {
        //    SubMenu navigation_user = menu.addSubMenu("Opciones Generales");
        //    tmp = navigation_user.add("Visualizar Rutas").setIcon(R.drawable.icon_route);
        //    menus.add(tmp);
        //    tmp = navigation_user.add("Ver Puntos").setIcon(R.drawable.icon_location);
        //    menus.add(tmp);
        //}

        SubMenu navigation_others = menu.addSubMenu("Otros");
        //tmp = navigation_others.add("Configuración");
        //tmp.setIcon(R.drawable.ic_baseline_settings_24);
        //menus.add(tmp);
        //tmp = navigation_others.add("Acerca de");
        //tmp.setIcon(R.drawable.ic_baseline_info_24);
        //menus.add(tmp);
        tmp = navigation_others.add("Cerrar Sesión");
        tmp.setIcon(R.drawable.ic_baseline_exit_to_app_24);
        tmp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Alerts.MessageToast(MainMenu.this, "cerrar sesión");
                st.setUsuario(null);
                onBackPressed();
                return false;
            }
        });

        for (MenuItem temp : menus) {
            temp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    loadFragment(new nodisponible_fragment());
                    return true;
                }
            });
        }
    }
}
