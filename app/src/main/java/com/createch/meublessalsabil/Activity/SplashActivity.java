package com.createch.meublessalsabil.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    Intent go_login;
    boolean blockedByAdmin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckLogged();
        Application.changeLang(Application.getCurrentLang(getApplicationContext()), this, false);
    }


    private void CheckLogged() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (user != null || isLoggedIn) {


            FirebaseDatabase.getInstance().getReference()
                    .child("BlockedUsers").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean isblock = true ;
                    if (!snapshot.exists())
                        isblock = false;
                    else {
                        isblock = true;
                        blockedByAdmin = snapshot.getValue(Boolean.class);
                    }

                    if (isblock) {
                        go_login = new Intent(SplashActivity.this, BlockedUserActivity.class);
                        go_login.putExtra("byadmin",blockedByAdmin);
                        go_login.putExtra("uid",user.getUid());

                    }
                    else
                        go_login = new Intent(SplashActivity.this, Home.class);


                    startActivity(go_login);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        } else {
            Intent go_login = new Intent(this, Login.class);
            startActivity(go_login);
            finish();

        }

    }







}