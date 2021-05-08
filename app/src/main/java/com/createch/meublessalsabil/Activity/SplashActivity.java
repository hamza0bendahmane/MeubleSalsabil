package com.createch.meublessalsabil.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.createch.meublessalsabil.R;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckLogged();
        Application.changeLang(Application.getCurrentLang(getApplicationContext()), this, false);
        setContentView(R.layout.activity_splash);
    }


    private void CheckLogged() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (user != null || isLoggedIn) {
            Intent go_login = new Intent(this, Home.class);
            startActivity(go_login);
            finish();


        } else {
            Intent go_login = new Intent(this, Login.class);
            startActivity(go_login);
            finish();

        }

    }

}