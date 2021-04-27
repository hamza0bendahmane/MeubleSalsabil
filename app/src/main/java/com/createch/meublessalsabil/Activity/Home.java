package com.createch.meublessalsabil.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.fragments.FavouritesFragment;
import com.createch.meublessalsabil.fragments.HomeFragment;
import com.createch.meublessalsabil.fragments.SettingFragment;
import com.createch.meublessalsabil.fragments.ShopListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    BottomNavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MeublesSalsabil);
        setContentView(R.layout.activity_home);
        navView = findViewById(R.id.nav_view);

        //FirebaseApp.initializeApp();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;
                int id = item.getItemId();
                if (id == R.id.navigation_home){
                    fragment = new HomeFragment();
                }else if (id == R.id.navigation_favourites){
                    fragment = new FavouritesFragment();
                }else if (id == R.id.navigation_shoplist){
                    fragment = new ShopListFragment();
                }else if (id == R.id.navigation_settings){
                    fragment = new SettingFragment();
                }

                return navigateToFragment(fragment);
            }
        });

        navView.setSelectedItemId(R.id.navigation_home);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        navView.setSelectedItemId(R.id.navigation_home);

    }

    private boolean navigateToFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void back(View v) {
        onBackPressed();
    }



}