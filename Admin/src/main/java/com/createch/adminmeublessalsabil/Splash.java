package com.createch.adminmeublessalsabil;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Splash extends AppCompatActivity {
    boolean ff = false;

    FragmentManager manager = getFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MeublesSalsabil);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (user == null) {

                    Intent go_login = new Intent(Splash.this, Login.class);
                    startActivity(go_login);
                    finish();

                } else {
                    db.collection("Admins").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {

                                Intent intent = new Intent(Splash.this, AdminPanelActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                firebaseAuth.signOut();
                                Intent go_login = new Intent(Splash.this, Login.class);
                                startActivity(go_login);
                                finish();
                            }
                        }
                    });
                }


            }
        }, 200);


    }

    private boolean AdminLogedin() {
        if (user == null) {
            Log.d("hbhb", "user is null");
            return false;
        } else {
            Log.d("hbhb", "aaaaaaa");

            db.collection("Admins").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ff = documentSnapshot.exists();
                }
            });
            return ff;
        }
    }
}