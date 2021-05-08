package com.createch.meublessalsabil.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.createch.meublessalsabil.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Register extends AppCompatActivity {

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPreferences;
    CallbackManager callbackManager;
    LoginButton loginButton;
    Activity activity;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;

    /*TDDO*/
    //save changed lang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.facebook_login_button);
        loginButton.setPermissions(Arrays.asList("email", "public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);
        activity = this;
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                handleThisToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {

                // App code
            }
        });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

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
                if (Application.getCurrentLang(getApplicationContext()) == "ar") {
                    Application.changeLang("fr", activity, true);
                } else {
                    Application.changeLang("ar", activity, true);
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
                        if(task.isSuccessful()) {
                            Map<String, Object> user = new HashMap<>();
                            FirebaseUser thisuser = task.getResult().getUser();
                            UserProfileChangeRequest userProf = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstName + " " + lastName)
                                    .setPhotoUri(Uri.parse("https://www.universal-trailers.co.uk/wp-content/uploads/2018/03/Person-placeholder.jpg"))
                                    .build();
                            thisuser.updateProfile(userProf);
                            user.put("fname", firstName);
                            user.put("lname", lastName);
                            user.put("email", email);
                            user.put("blocked", false);
                            user.put("photo", "https://www.universal-trailers.co.uk/wp-content/uploads/2018/03/Person-placeholder.jpg");
                            user.put("phone", "");
                            user.put("adr", null);

                            //Adding document to firestore
                            db.collection("Users")
                                    .document(task.getResult().getUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                                    finish();
                                                }
                                            });

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
        Intent refresh = new Intent(getApplicationContext(), Register.class);
        startActivity(refresh);
        finish();
    }

    private String getCurrentLang() {
        String language = getResources().getConfiguration().locale.getLanguage();
        return language;
    }

    private void handleThisToken(AccessToken token) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(token.getToken());
        fAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                    Map<String, Object> user = new HashMap<>();
                    user.put("fname", task.getResult().getUser().getDisplayName().split(" ")[0]);
                    user.put("lname", task.getResult().getUser().getDisplayName().split(" ")[1]);
                    user.put("email", task.getResult().getUser().getEmail());
                    user.put("photo", String.valueOf(task.getResult().getUser().getPhotoUrl()));
                    user.put("phone", task.getResult().getUser().getPhoneNumber());
                    user.put("adr", null);

                    //Adding document to firestore
                    db.collection("Users")
                            .document(task.getResult().getUser().getUid())
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("hbhb", "gggggggg");

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("hbhb", "Error adding document", e);
                        }
                    });


                } else {
                    Log.d("hbhb", "message : " + task.getException().getMessage());
                    Log.d("hbhb", "cause : " + task.getException().getCause());
                }
            }
        });
    }

    private boolean isPasswordValid(String text) {
        return text != null && text.length() >= 8;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();

    }
}