package com.createch.adminmeublessalsabil.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.adminmeublessalsabil.Model.User;
import com.createch.adminmeublessalsabil.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetails extends AppCompatActivity {
String refer ;
MaterialButton cancel , submit;
boolean isAccepted ;
DocumentReference orderReference;
TextView name ,number ,phone ,email , infos ,price , order_id , date ,time ;
RecyclerView soldables ;
CircleImageView user_image ;
User who_makes_order;
ImageView about_user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        refer = getIntent().getStringExtra("ref");
        FirebaseApp.initializeApp();
        isAccepted = getIntent().getBooleanExtra("accepted",false);
        orderReference = FirebaseFirestore.getInstance().collection("Orders").document(refer);
        setState();
        filltheFields();



    }

    private void setState() {
        cancel = findViewById(R.id.cancel_name);
        submit = findViewById(R.id.submit_name);

        if(isAccepted){
            submit.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
        }else{
            submit.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);


        }
    }

    private void filltheFields() {

        price = findViewById(R.id.price);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        order_id = findViewById(R.id.order_number);
        infos = findViewById(R.id.adresses);
        phone = findViewById(R.id.materials);
        name = findViewById(R.id.name_product);
        email = findViewById(R.id.text_email);
        user_image = findViewById(R.id.image_product);
        about_user = findViewById(R.id.im3);
        soldables = findViewById(R.id.soldables);
        orderReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                who_makes_order = (User) documentSnapshot.get("user");
                order_id.setText(documentSnapshot.getId());
                price.setText(documentSnapshot.get("totalPrice").toString());
                time.setText(String.valueOf(documentSnapshot.getTimestamp("date").toDate().getTime()));
                date.setText(documentSnapshot.getTimestamp("date").toDate().toString());
                infos.setText(new StringBuilder().append(who_makes_order.getWilaya()).append(" ,").append(who_makes_order.getAdr()).toString());
                name.setText(who_makes_order.getFname());
                email.setText(who_makes_order.getEmail());
                phone.setText(who_makes_order.getPhone());
                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(250, 200) ;
                Glide.with(getApplicationContext()).load(who_makes_order.getPhoto()).apply(requestOptions).into(user_image);
            }
        });

        about_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tools = findViewById(R.id.edit_user);
                if (tools.getVisibility() == View.GONE)
                    tools.setVisibility(View.VISIBLE);
                else
                    tools.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.mail_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + who_makes_order.getEmail())); // only email apps should handle this
                startActivity( intent);

            }
        });
        findViewById(R.id.phone_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call user
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + who_makes_order.getPhone())); // only email apps should handle this
                startActivity( intent);
            }
        });
        findViewById(R.id.block_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // block user

                try {
                 String uid =   FirebaseAuth.getInstance().getUserByEmail(who_makes_order.getEmail()).getUid();
                    new AlertDialog.Builder(getApplicationContext()).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // block user
                            FirebaseFirestore.getInstance().collection("Users").document(uid).update("blocked", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseFirestore.getInstance().collection("BlockedUsers").document(uid).set(new HashMap<String, Object>(), SetOptions.merge()).
                                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "blocked", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            });

                        }
                    })
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setTitle("تأكيد").
                            setMessage("هل انت متأكد من حظر المستخدم").show();



                } catch (FirebaseAuthException e) {
                    e.printStackTrace();
                }


            }
        });





    }
    public void cancelOrder(View vv){
        new AlertDialog.Builder(this).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orderReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(vv,"تم حذف الطلب",Snackbar.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
            }
        })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setTitle("تأكيد").
                setMessage("هل انت متأكد من حذف هذا الطلب").show();


    }
    public void submitOrder(View vv){
        orderReference.update("state","accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Snackbar.make(vv,"تم قبول الطلب",Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}