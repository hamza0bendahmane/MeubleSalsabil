package com.createch.meublessalsabil.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.createch.meublessalsabil.R;
import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText registeredEmail;
    private Button resetPasswordButton;
    private ViewGroup emailIconContainer;
    private ImageView emailIcon;
    private TextView emailIconText;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    public ForgetPassword() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Inflate the layout for this fragment
        registeredEmail = findViewById(R.id.forgot_password_email);
        resetPasswordButton = findViewById(R.id.reset_password_btn);
        emailIconContainer = findViewById(R.id.forgot_password_email_container);
        emailIcon = findViewById(R.id.forgot_password_email_icon);
        emailIconText = findViewById(R.id.forgot_password_email_icon_text);
        progressBar = findViewById(R.id.forgot_password_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();


        registeredEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIconText.setVisibility(View.GONE);


                TransitionManager.beginDelayedTransition(emailIconContainer);
                emailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                resetPasswordButton.setEnabled(false);
                resetPasswordButton.setTextColor(Color.argb(50, 255, 255, 255));

                firebaseAuth.sendPasswordResetEmail(registeredEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, emailIcon.getWidth() / 2, emailIcon.getHeight() / 2);
                            scaleAnimation.setDuration(100);
                            scaleAnimation.setInterpolator(new AccelerateInterpolator());
                            scaleAnimation.setRepeatMode(Animation.REVERSE);
                            scaleAnimation.setRepeatCount(1);
                            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    emailIconText.setText("Recovery Email Sent Successfully! Please Check your Inbox");
                                    emailIconText.setTextColor(Color.GREEN);
                                    TransitionManager.beginDelayedTransition(emailIconContainer);
                                    emailIcon.setVisibility(View.VISIBLE);
                                    emailIconText.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                    emailIcon.setImageResource(R.drawable.email_icon);
                                    emailIcon.setBackgroundColor(Color.GREEN);
                                }
                            });

                            emailIcon.startAnimation(scaleAnimation);

                            //Toast.makeText(getActivity(), "Email Sent Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            String error = task.getException().getMessage();
                            resetPasswordButton.setEnabled(true);
                            resetPasswordButton.setTextColor(Color.rgb(255, 255, 255));
                            emailIcon.setImageResource(R.drawable.email_icon);
                            emailIcon.setBackgroundColor(Color.RED);
                            emailIconText.setText(error);
                            emailIconText.setTextColor(Color.RED);
                            TransitionManager.beginDelayedTransition(emailIconContainer);
                            emailIconText.setVisibility(View.VISIBLE);
                            // Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                });
            }
        });


    }

    private void checkInputs() {
        if (TextUtils.isEmpty(registeredEmail.getText())) {
            resetPasswordButton.setEnabled(false);
            resetPasswordButton.setTextColor(Color.argb(50, 255, 255, 255));
        } else {
            resetPasswordButton.setEnabled(true);
            resetPasswordButton.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    private void backLogin() {
        startActivity(new Intent(this, Login.class));
        finish();
    }

}