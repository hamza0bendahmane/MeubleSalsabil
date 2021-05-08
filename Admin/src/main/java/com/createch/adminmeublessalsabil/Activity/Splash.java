package com.createch.adminmeublessalsabil.Activity;


import android.app.FragmentManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Splash extends AppCompatActivity {

    FragmentManager manager = getFragmentManager();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MeublesSalsabil);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
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
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        firebaseAuth.signOut();
                        Intent go_login = new Intent(Splash.this, Login.class);
                        startActivity(go_login);
                        finish();
                    }
                });
            }


        }, 200);


    }


}