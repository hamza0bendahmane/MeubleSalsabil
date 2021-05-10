package com.createch.meublessalsabil.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.fragments.OrderDetails;
import com.createch.meublessalsabil.models.Order;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class OrdersAdapter extends FirestoreRecyclerAdapter<Order, OrdersAdapter.OrderHolder> {

    Context context;

    public OrdersAdapter(@NonNull FirestoreRecyclerOptions<Order> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrdersAdapter.OrderHolder holder, int position, @NonNull Order model) {
        holder.setProductPrice(String.valueOf(model.getTotalPrice()));
        holder.setChanges(model.getSoldItems());
        holder.chngeStatus(model.getState());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    public class OrderHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView productPrice;
        TextView state;
        TextView productQuantity;
        MaterialCardView productColor;
        TextView productMaterial;
        ImageView productImage;
        MaterialButton details;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.pimg);
            details = itemView.findViewById(R.id.button_order_onhold);
            productMaterial = itemView.findViewById(R.id.materia);
            productColor = itemView.findViewById(R.id.color_c);
            productQuantity = itemView.findViewById(R.id.quantity_prod);
            state = itemView.findViewById(R.id.order_delivered);
            productPrice = itemView.findViewById(R.id.price);
            productName = itemView.findViewById(R.id.namecatroduct);

        }


        public void setProductColor(String productColor) {
            this.productColor.setCardBackgroundColor(Color.parseColor(productColor));

        }

        public void setProductName(String productColor) {
            this.productName.setText(productColor);

        }


        public void setProductMaterial(String m) {
            productMaterial.setText(m);

        }

        public void setProductPrice(String m) {
            productPrice.setText(m);

        }

        public void setProductQuantity(String q) {
            productQuantity.setText(q);

        }

        public void setProductImage(String q) {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter();
            Glide.with(context).load(Uri.parse(q)).apply(requestOptions).
                    into(productImage);
        }


        public void setChanges(String refer) {

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl(refer);
            dbRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    long number = dataSnapshot.getChildrenCount();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String reference = child.getKey();
                        HashMap<String, Object> soldable = (HashMap<String, Object>) child.getValue();
                        setProductColor(String.valueOf(soldable.get("color")));
                        setProductMaterial(String.valueOf(soldable.get("material")));
                        setProductQuantity(String.valueOf(soldable.get("quantity")));
                        FirebaseFirestore.getInstance().collection("Products")
                                .document(reference).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                setProductImage(documentSnapshot.getString("image"));
                                setProductName(documentSnapshot.getString("name"));

                            }
                        });


                    }
                }
            });
        }

        public void chngeStatus(String statut) {

            if (statut.equals(Order.DELIVERED)) {
                state.setText("* تم تسليم الطلب *");
                state.setVisibility(View.VISIBLE);
                details.setVisibility(View.GONE);
            } else if (statut.equals(Order.ONWAY)) {
                state.setText("* الطلب في طريقه للتسليم *");
                state.setVisibility(View.VISIBLE);
                details.setVisibility(View.GONE);
            } else {
                state.setVisibility(View.GONE);
                details.setVisibility(View.VISIBLE);

            }

        }
    }


}
