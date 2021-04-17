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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Login extends AppCompatActivity {

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences;
    private TextInputEditText uEmail, uPassword;
    private TextView uRegisterButton;
    private Button uLoginBtn;

    /*TDDO*/
    //save changed lang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MeublesSalsabil);
        setContentView(R.layout.activity_login);

        uEmail = (TextInputEditText) findViewById(R.id.email_edit_text);
        uPassword = (TextInputEditText) findViewById(R.id.password_edit_text);
        uRegisterButton = (TextView) findViewById(R.id.textView);
        uLoginBtn = (Button) findViewById(R.id.login_button);

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

        uLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = uEmail.getText().toString().trim();
                String password = uPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    //emailTextInput.setError(getString(R.string.email_error));
                    //Toast.makeText(getApplicationContext(), getString(R.string.email_error), Toast.LENGTH_SHORT).show();
                    uEmail.requestFocus();
                    uEmail.setError(getString(R.string.email_error));
                    return;
                }

                if (!isPasswordValid(password)){
                    //passwordTextInput.setError(getString(R.string.password_error));
                    //Toast.makeText(getApplicationContext(), getString(R.string.password_error), Toast.LENGTH_SHORT).show();
                    uPassword.requestFocus();
                    uPassword.setError(getString(R.string.password_error));
                }else{

                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d("firebase---","success login");
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                finish();
                            }
                            else{
                                Log.d("firebase---","fail login",task.getException());
                                Toast.makeText(getApplicationContext(), "Email or password is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        uRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
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
        Intent refresh = new Intent (getApplicationContext(), Login.class);
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