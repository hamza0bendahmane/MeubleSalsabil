package com.createch.adminmeublessalsabil.Activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.createch.adminmeublessalsabil.Fragment.OrdersFragment;
import com.createch.adminmeublessalsabil.Fragment.PanelFragment;
import com.createch.adminmeublessalsabil.Fragment.ProductsFragment;
import com.createch.adminmeublessalsabil.Fragment.SettingsFragment;
import com.createch.adminmeublessalsabil.Fragment.UserFragment;
import com.createch.adminmeublessalsabil.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


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
                    .replace(R.id.nav_host_fragment, fragment).addToBackStack("app")
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

        getSupportFragmentManager().popBackStackImmediate();

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            Fragment selectedFragment = null;
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    selectedFragment = fragment;
                    break;
                }
            }
            if (selectedFragment instanceof SettingsFragment) {
                navView.setSelectedItemId(R.id.idsettings);

            }
            if (selectedFragment instanceof ProductsFragment) {
                navView.setSelectedItemId(R.id.idproducts);

            }
            if (selectedFragment instanceof OrdersFragment) {
                navView.setSelectedItemId(R.id.idorders);

            }
            if (selectedFragment instanceof UserFragment) {
                navView.setSelectedItemId(R.id.idusers);

            }
            if (selectedFragment instanceof PanelFragment) {
                existDialog();
            }

        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1)
            existDialog();
        else {
            super.onBackPressed();
        }


    }

    private void existDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}