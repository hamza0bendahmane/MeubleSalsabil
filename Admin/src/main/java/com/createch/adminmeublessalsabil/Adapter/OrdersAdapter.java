package com.createch.adminmeublessalsabil.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.adminmeublessalsabil.Fragment.OrderDetails;
import com.createch.adminmeublessalsabil.Model.Order;
import com.createch.adminmeublessalsabil.Model.User;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersAdapter extends FirestoreRecyclerAdapter<Order, OrdersAdapter.OrderHolder> {
    Context context;

    public OrdersAdapter(@NonNull FirestoreRecyclerOptions<Order> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull OrdersAdapter.OrderHolder holder,
                                    int position, @NonNull Order model) {


        FirebaseFirestore.getInstance().collection("Users").document(model.getUserId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User thisuser = documentSnapshot.toObject(User.class);
                        
                        holder.setName(thisuser.getFname());
                        holder.setPhone(thisuser.getPhone());
                        holder.setPhoto(thisuser.getPhoto());
                    }
                });
   holder.setDate(((Timestamp) model.getDate().get(Order.WAITING)).toDate().toString());
        


        holder.itemView.findViewById(R.id.order_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Bundle bundle = new Bundle();
                bundle.putString("ref", getSnapshots().getSnapshot(position).getId());
                bundle.putString("items", model.getSoldItems());
                bundle.putString("state", model.getState());
                Fragment fragment = new OrderDetails();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.nav_host_fragment, fragment).addToBackStack("app")
                        .commit();

            }
        });



    }


    @NonNull
    @Override
    public OrdersAdapter.OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,
                parent, false);
        return new OrdersAdapter.OrderHolder(v);
    }


    class OrderHolder extends RecyclerView.ViewHolder {

        // creating variables for our text views.
        TextView name;
        TextView date;
        TextView phone;
        CircleImageView photo;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            photo = itemView.findViewById(R.id.photo);
            date = itemView.findViewById(R.id.date);

        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setDate(String name) {
            this.date.setText(name);
        }
        public void setPhone(String phone) {
            this.phone.setText(phone);
        }

        public void setPhoto(String photo) {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(250, 200) ;
            Glide.with(context).load(Uri.parse(photo)).apply(requestOptions).into(this.photo);
        }




    }


}