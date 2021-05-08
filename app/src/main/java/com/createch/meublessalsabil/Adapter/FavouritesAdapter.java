package com.createch.meublessalsabil.Adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.fragments.ProductShow;
import com.createch.meublessalsabil.models.Item;
import com.createch.meublessalsabil.models.Promotion;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FavouritesAdapter extends FirebaseRecyclerAdapter<String, FavouritesAdapter.ProductHolder> {
    Context context;
    Item model;
    Promotion prom_model;

    public FavouritesAdapter(@NonNull FirebaseRecyclerOptions<String> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull String mode) {


        FirebaseFirestore.getInstance().collection("Products")
                .document(getSnapshots().getSnapshot(position).getKey()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                model = documentSnapshot.toObject(Item.class);
                if (!model.getPromotion().isEmpty()) {
                    FirebaseFirestore.getInstance().collection("Promotions")
                            .document(model.getPromotion()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            prom_model = documentSnapshot.toObject(Promotion.class);
                            holder.updatePromtionView(String.valueOf(prom_model.getDiscount()), prom_model.getEndDate());
                            holder.setProductPrice(model.getPrice(), prom_model.getDiscount());

                        }
                    });
                } else
                    holder.setProductPrice(model.getPrice(), 0);

                holder.setProductColors(model.getColors());
                holder.setProductImage(model.getImage());
                holder.setProductCategory(model.getCategory());
                holder.setProductMaterials(model.getMaterials());
                holder.setProductName(model.getName());
            }
        });


        holder.itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getSnapshots().getSnapshot(position).getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(v, "تم الحذف ", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Bundle bundle = new Bundle();
                bundle.putString("ref", getSnapshots().getSnapshot(position).getKey());
                bundle.putString("promotion", model.getPromotion());
                Fragment fragment = new ProductShow();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }
        });
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_layout,
                parent, false);
        return new ProductHolder(v);
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView productCategory;
        TextView productMaterials;
        ImageView productImage;
        RecyclerView productColors;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.pimg);
            productColors = itemView.findViewById(R.id.colors);
            productName = itemView.findViewById(R.id.namecatroduct);
            productCategory = itemView.findViewById(R.id.cat);
            productMaterials = itemView.findViewById(R.id.material);
            productPrice = itemView.findViewById(R.id.price);
        }

        public void setProductName(String productName) {
            this.productName.setText(productName);
        }

        public void setProductCategory(String cat) {
            this.productCategory.setText(cat);
        }

        public void setProductPrice(double productPrice, int discount) {
            double fprice = productPrice - (productPrice * discount / 100);
            this.productPrice.setText(fprice + " " + context.getString(R.string.currency));
        }

        public void setProductMaterials(List<String> productMaterials) {
            this.productMaterials.setText(productMaterials.toString().substring(1, productMaterials.toString().length() - 1));
        }

        public void setProductImage(String productImage) {

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(Uri.parse(productImage)).apply(requestOptions).into(this.productImage);

        }

        public void setProductColors(List<String> productColors) {
            ColorsAdapter ada = new ColorsAdapter(context, productColors, false);
            this.productColors.setHasFixedSize(true);
            this.productColors.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            this.productColors.setAdapter(ada);
        }

        public void updatePromtionView(String valueOf, String endDate) {


        }
    }

}