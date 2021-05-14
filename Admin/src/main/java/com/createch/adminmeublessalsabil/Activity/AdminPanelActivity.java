package com.createch.adminmeublessalsabil.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.createch.adminmeublessalsabil.Fragment.OrdersFragment;
import com.createch.adminmeublessalsabil.Fragment.PanelFragment;
import com.createch.adminmeublessalsabil.Fragment.ProductsFragment;
import com.createch.adminmeublessalsabil.Fragment.SettingsFragment;
import com.createch.adminmeublessalsabil.Fragment.UserFragment;
import com.createch.adminmeublessalsabil.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class AdminPanelActivity extends AppCompatActivity {
    BottomNavigationView navView;
    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                int id = item.getItemId();
                if (id == R.id.idsettings) {
                    fragment = new SettingsFragment();
                } else if (id == R.id.idorders) {
                    fragment = new OrdersFragment();
                } else if (id == R.id.idusers) {
                    fragment = new UserFragment();
                } else if (id == R.id.idproducts) {
                    fragment = new ProductsFragment();
                } else if (id == R.id.idpanel) {
                    fragment = new PanelFragment();
                }

                return showFragment(fragment);
            }
        });
        navView.setSelectedItemId(R.id.idpanel);

    }

    private boolean showFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void gotoorders(View vv) {

        navView.setSelectedItemId(R.id.idorders);
    }

    public void gotoaorders(View vv) {
        navView.setSelectedItemId(R.id.idorders);


    }

    public void gotoproducts(View vv) {

        navView.setSelectedItemId(R.id.idproducts);

    }

    public void gotousers(View vv) {

        navView.setSelectedItemId(R.id.idusers);

    }

    @Override
    public void onBackPressed() {
        int id = navView.getSelectedItemId();
        if (id == R.id.idorders) {
            navView.setSelectedItemId(R.id.idproducts);

        } else if (id == R.id.idproducts) {
            navView.setSelectedItemId(R.id.idusers);

        } else if (id == R.id.idusers) {
            navView.setSelectedItemId(R.id.idpanel);

        } else {
            super.onBackPressed();

        }
    }


}