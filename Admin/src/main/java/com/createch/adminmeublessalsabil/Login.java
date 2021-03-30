package com.createch.adminmeublessalsabil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

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

public class Login extends AppCompatActivity {
    boolean ff = false;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MeublesSalsabil);
        setContentView(R.layout.activity_login);
        SwitchCompat lang_switch = findViewById(R.id.lang_switch);


        lang_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getLang() == "ar") {
                    changeLang("fr");
                } else {
                    changeLang("ar");

                }
            }
        });
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

    private boolean checkPermission(String email) {

        db.collection("Admins").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ff = documentSnapshot.exists();
            }
        });
        return ff;

    }
}