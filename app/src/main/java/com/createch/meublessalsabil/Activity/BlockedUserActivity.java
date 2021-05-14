package com.createch.meublessalsabil.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.createch.meublessalsabil.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class BlockedUserActivity extends AppCompatActivity {
    boolean blockedByAdmin ;
    TextView sorry ;
    Button unblock ;
    String adminMail ;
    String uid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blockedByAdmin = getIntent().getBooleanExtra("byadmin",true);
        uid = getIntent().getStringExtra("uid");
        setContentView(R.layout.activity_blocked_user);
        sorry = findViewById(R.id.sorry);
        unblock = findViewById(R.id.unblock);


        if (blockedByAdmin){
            unblock.setText(R.string.send_objection);

        }else
        {
            sorry.setVisibility(View.GONE);
            unblock.setText(R.string.reactivate_account);
        }
        unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blockedByAdmin){
                    sendObjection();
                }else{
                Reactivate();
                }
            }
        });

    }

    private void sendObjection() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + adminMail)); // only email apps should handle this
        startActivity( intent);

    }

    private void Reactivate() {
        FirebaseDatabase.getInstance().getReference()
                .child("BlockedUsers").child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),R.string.succ,Toast.LENGTH_SHORT).show();
            }
        });
    }
}