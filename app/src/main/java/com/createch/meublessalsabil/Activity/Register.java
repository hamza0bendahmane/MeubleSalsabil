package com.createch.meublessalsabil.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
                            user.put("adresse", new Adresse());

                            //Adding document to firestore
                            db.collection("Users")
                                    .document(task.getResult().getUser().getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
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

        if(fAuth.getCurrentUser()!=null) {
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