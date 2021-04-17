package com.createch.meublessalsabil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Register extends AppCompatActivity {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPreferences;

    /*TDDO*/
    //save changed lang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MeublesSalsabil);
        setContentView(R.layout.activity_register);

        TextInputLayout passwordTextInput, emailTextInput, lastNameTextInput, firstNameTextInput;
        TextInputEditText uFirstName = findViewById(R.id.name_edit_text);
        TextInputEditText uLastName = findViewById(R.id.lastname_edit_text);
        TextInputEditText uEmail = findViewById(R.id.email_edit_text);
        TextInputEditText uPassword = findViewById(R.id.password_edit_text);
        Button uRegisterButton = findViewById(R.id.register_button);
        TextView uLoginBtn = findViewById(R.id.textView);

        //change languange
        Button langBtn = findViewById(R.id.change_lang);
        langBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCurrentLang() == "ar") {
                    changeLang("fr");
                } else {
                    changeLang("ar");
                }
            }
        });

        uRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = uFirstName.getText().toString().trim();
                String lastName = uLastName.getText().toString().trim();
                String email = uEmail.getText().toString().trim();
                String password = uPassword.getText().toString().trim();

                if(TextUtils.isEmpty(firstName)){
                    uFirstName.requestFocus();
                    uFirstName.setError("this field must not be empty");
                    return;
                }

                if(TextUtils.isEmpty(lastName)){
                    uLastName.requestFocus();
                    uLastName.setError("this field must not be empty");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    uEmail.requestFocus();
                    uEmail.setError(getString(R.string.email_error));
                    return;
                }

                if (!isPasswordValid(password)){
                    uPassword.requestFocus();
                    uPassword.setError(getString(R.string.password_error));
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("firebase---","success signup");

                            //Creating new user
                            /*TODO*/
                            //replace with User model
                            Map<String, Object> user = new HashMap<>();
                            user.put("first", firstName);
                            user.put("last", lastName);
                            user.put("email",email);
                            //user.put("imageUrl","_");
                            //user.put("phone","_");
                            //user.put("address","_");

                            //Adding document to firestore
                            db.collection("Users")
                                    .document(fAuth.getCurrentUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Firestore---", "DocumentSnapshot added with ID: " + fAuth.getCurrentUser().getUid());
                                            startActivity(new Intent(getApplicationContext(), Home.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Firestore---", "Error adding document", e);
                                }
                            });
                        }
                        else{
                            Log.d("firebase---","fail signup",task.getException());
                            Toast.makeText(getApplicationContext(), "Email or password is wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        uLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), Home.class));
        }

    }

    private void changeLang(String lang){
        Locale local = new Locale(lang);
        Resources res = getBaseContext().getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        Configuration configuration = res.getConfiguration();
        configuration.locale = local;
        res.updateConfiguration(configuration, displayMetrics);
        Toast.makeText(this, getResources().getString(R.string.lang_updated), Toast.LENGTH_SHORT).show();
        Intent refresh = new Intent (getApplicationContext(), Register.class);
        startActivity(refresh);
        finish();
    }

    private String getCurrentLang(){
        String language = getResources().getConfiguration().locale.getLanguage();
        return language;
    }

    private boolean isPasswordValid(String text) {
        return text != null && text.length() >= 8;
    }
}