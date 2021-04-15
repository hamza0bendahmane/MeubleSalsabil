package com.createch.adminmeublessalsabil.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.adminmeublessalsabil.Activity.OrderDetails;
import com.createch.adminmeublessalsabil.Model.Order;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

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


        holder.setName(model.getUser().getFname());
        holder.setPhone(model.getUser().getPhone());
        holder.setPhoto(model.getUser().getPhoto());


        holder.itemView.findViewById(R.id.order_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Intent ii = new Intent(context, OrderDetails.class);
                        ii.putExtra("ref", getSnapshots().getSnapshot(position).getId());
                        ii.putExtra("accepted", model.getState().equals("accepted"));

                context.startActivity(ii);

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
        TextView phone;
        CircleImageView photo;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            photo = itemView.findViewById(R.id.photo);
        }

        public void setName(String name) {
            this.name.setText(name);
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