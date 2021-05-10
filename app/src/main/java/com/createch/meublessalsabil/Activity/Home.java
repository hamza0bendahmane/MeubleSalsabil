package com.createch.meublessalsabil.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.fragments.FavouritesFragment;
import com.createch.meublessalsabil.fragments.HomeFragment;
import com.createch.meublessalsabil.fragments.SettingFragment;
import com.createch.meublessalsabil.fragments.ShopListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Home extends AppCompatActivity {
    public BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Navigate();

    }

    private void Navigate() {
        navView = findViewById(R.id.nav_view);

        //FirebaseApp.initializeApp();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    fragment = new HomeFragment();
                } else if (id == R.id.navigation_favourites) {
                    fragment = new FavouritesFragment();
                } else if (id == R.id.navigation_shoplist) {
                    fragment = new ShopListFragment();
                } else if (id == R.id.navigation_settings) {
                    fragment = new SettingFragment();
                }

                return navigateToFragment(fragment);
            }
        });

        navView.setSelectedItemId(R.id.navigation_home);

    }

    private boolean navigateToFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment).addToBackStack("app")
                    .commit();
            return true;
        }
        return false;
    }

    public void back(View v) {
        onBackPressed();

    }

    @Override
    public void onBackPressed() {

        int fragmens
                = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmens == 1) {
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


        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStackImmediate();
                Fragment selectedFragment = null;
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isVisible()) {
                        selectedFragment = fragment;
                        break;
                    }
                }
                if (selectedFragment instanceof FavouritesFragment) {
                    navView.setSelectedItemId(R.id.navigation_favourites);
                }
                if (selectedFragment instanceof HomeFragment) {
                    navView.setSelectedItemId(R.id.navigation_home);
                }
                if (selectedFragment instanceof SettingFragment) {
                    navView.setSelectedItemId(R.id.navigation_settings);
                }
                if (selectedFragment instanceof ShopListFragment) {
                    navView.setSelectedItemId(R.id.navigation_shoplist);
                } else {
                    super.onBackPressed();
                }

            } else {
                super.onBackPressed();
            }


        }

    }
}