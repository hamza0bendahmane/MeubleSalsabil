package com.createch.adminmeublessalsabil.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.createch.adminmeublessalsabil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class Login extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    boolean ff = false;
    Spinner lang_switch;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] languages = {"français", "عربي"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setTheme(R.style.Theme_MeublesSalsabil);
        setContentView(R.layout.activity_login);
        lang_switch = findViewById(R.id.spinner_lang);
        lang_switch.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        lang_switch.setAdapter(aa);

    }

    private void changeLang(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getBaseContext().getResources();
        DisplayMetrics display = res.getDisplayMetrics();
        Configuration configuration = res.getConfiguration();
        configuration.locale = myLocale;
        res.updateConfiguration(configuration, display);
        Toast.makeText(this, getResources().getString(R.string.language_updated), Toast.LENGTH_SHORT).show();
        Intent refresh = new Intent(getApplicationContext(), Login.class);
        startActivity(refresh);
        finish();
    }

    private String getLang() {
        String language = getResources().getConfiguration().locale.getLanguage();
        Log.d("hbhb", language);

        return language;
    }

    public void enterToPanel(View vv) {
        TextInputEditText SignUpMail = findViewById(R.id.email_edit_text);
        TextInputEditText SignUpPass = findViewById(R.id.password_edit_text);
        String email = SignUpMail.getText().toString().trim();
        final String pass = SignUpPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            SignUpMail.requestFocus();
            SignUpMail.setError(getString(R.string.empty_email));
        }
        if (TextUtils.isEmpty(pass)) {
            SignUpPass.requestFocus();
            SignUpPass.setError(getString(R.string.empty_password));
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(Login.this);
            progressDialog.setTitle(R.string.please_wait);
            progressDialog.setCancelable(false);
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, R.string.error + task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            } else {


                                db.collection("Admins").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            Snackbar.make(findViewById(R.id.button_login), getString(R.string.login_successful), Snackbar.LENGTH_LONG).show();
                                            // GO TO PANEL

                                            Intent intent = new Intent(Login.this, AdminPanelActivity.class);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                            finish();
                                        } else {
                                            firebaseAuth.signOut();
                                            progressDialog.dismiss();
                                            Snackbar.make(findViewById(R.id.button_login), getString(R.string.not_admin), Snackbar.LENGTH_LONG).show();

                                        }
                                    }
                                });
                                SignUpMail.setText("");
                                SignUpPass.setText("");
                            }
                        }
                    });
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            if (getLang() == "ar") {
                changeLang("fr");
            }

        } else {
            if (!(getLang() == "ar")) {
                changeLang("ar");

            }

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}