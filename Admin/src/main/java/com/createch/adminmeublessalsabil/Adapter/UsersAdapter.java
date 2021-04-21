package com.createch.adminmeublessalsabil.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.adminmeublessalsabil.Model.User;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.startActivity;

public class UsersAdapter extends FirestoreRecyclerAdapter<User, UsersAdapter.UserHolder> {
    Context context;

    public UsersAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User model) {

        DocumentSnapshot thisdoc = getSnapshots().getSnapshot(position);
        DocumentReference thisdocref = thisdoc.getReference();
        holder.setName(model.getFname());
        holder.setMail(model.getEmail());
        holder.setPhone(model.getPhone());
        holder.setPhoto(model.getPhoto());

        holder.itemView.findViewById(R.id.name_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tools = holder.itemView.findViewById(R.id.edit_user);
                if (tools.getVisibility() == View.GONE)
                    tools.setVisibility(View.VISIBLE);
                else
                    tools.setVisibility(View.GONE);
            }
        });
        holder.itemView.findViewById(R.id.im3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : show the user detailed infos ...


            }
        });
        holder.itemView.findViewById(R.id.mail_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + model.getEmail())); // only email apps should handle this
                startActivity(context, intent, null);

            }
        });
        holder.itemView.findViewById(R.id.phone_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call user
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + model.getPhone())); // only email apps should handle this
                startActivity(context, intent, null);
            }
        });
        holder.itemView.findViewById(R.id.block_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // block user
                        thisdocref.update("blocked", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseFirestore.getInstance().collection("BlockedUsers").document(thisdocref.getId()).set(new HashMap<String, Object>(), SetOptions.merge()).
                                        addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(holder.itemView.getContext(), "blocked", Toast.LENGTH_SHORT).show();

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






            }
        });


    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout,
                parent, false);
        return new UserHolder(v);
    }


    class UserHolder extends RecyclerView.ViewHolder {
        TextView mail;
        TextView phone;
        TextView name;
        CircleImageView photo;

        public UserHolder(View itemView) {
            super(itemView);
            mail = itemView.findViewById(R.id.text_email);
            phone = itemView.findViewById(R.id.materials);
            name = itemView.findViewById(R.id.name_product);
            photo = itemView.findViewById(R.id.image_product);
        }

        public void setMail(String mail) {
            this.mail.setText(mail);
        }

        public void setPhone(String phone) {
            this.phone.setText(phone);

        }

        public void setName(String name) {
            this.name.setText(name);

        }

        public void setPhoto(String photo) {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(100, 100) ;
            Glide.with(context).load(Uri.parse(photo)).apply(requestOptions).into(this.photo);
        }

    }
}