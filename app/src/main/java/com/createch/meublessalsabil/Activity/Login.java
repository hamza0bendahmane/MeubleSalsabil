package com.createch.meublessalsabil.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Adresse;
import com.createch.meublessalsabil.models.User;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    SharedPreferences sharedPreferences;
    private TextInputEditText uEmail, uPassword;
    private TextView uRegisterButton;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button uLoginBtn;
    ProgressDialog mProgressDialog;
    CallbackManager callbackManager;
    LoginButton loginButton;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        activity = this;
        loginButton = findViewById(R.id.facebook_login_button);
        loginButton.setPermissions(Arrays.asList("email", "public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);

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
                Log.d("hbhb", "canceled fb ");
            }

            @Override
            public void onError(FacebookException exception) {
                //  printKeyHash();
                // App code
                Log.d("hbhb", "fb exception : " + exception.getMessage() + " " + exception.getCause());
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
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };
        uEmail = findViewById(R.id.email_edit_text);
        uPassword = findViewById(R.id.password_edit_text);
        uRegisterButton = findViewById(R.id.textView);
        uLoginBtn = findViewById(R.id.login_button);

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
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

    }

    private void handleThisToken(AccessToken token) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(token.getToken());
        fAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                    if (isNewUser) {
                        RegisterNewUser(task.getResult().getUser());
                    } else {
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        finish();
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("hbhb", "signInWithCredential:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    //TODO: HANDLE BAD LOGIN
                }


            }
        });
    }

    private void RegisterNewUser(FirebaseUser user) {
        User userdata = new User(user.getDisplayName().split(" ")[0], user.getDisplayName().split(" ")[1], user.getEmail(),
                user.getPhoneNumber(), new Adresse(), user.getPhotoUrl() + "/picture?height=500", false);
        FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid()).set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();

            }
        });
    }

    public void resetPass(View vv) {
        startActivity(new Intent(this, ForgetPassword.class));
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.com_facebook_loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
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
        profileTracker.stopTracking();
    }
}