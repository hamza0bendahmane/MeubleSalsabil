package com.createch.meublessalsabil.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Item;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FavouritesAdapter extends FirestoreRecyclerAdapter<Item, FavouritesAdapter.ProductHolder> {
    Context context;

    public FavouritesAdapter(@NonNull FirestoreRecyclerOptions<Item> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Item model) {
        holder.setProductColors(model.getColors());
        holder.setProductImage(model.getImage());
        holder.setProductMaterials(model.getMaterials());
        holder.setProductName(model.getName());
        holder.setProductPrice(String.valueOf(model.getPrice()));

        holder.itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "تم الحذف ", Snackbar.LENGTH_SHORT).show();

                /*
                getSnapshots().getSnapshot(position).getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(v,"تم الحذف ",Snackbar.LENGTH_SHORT).show();
                    }
                });*/
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
        TextView productMaterials;
        ImageView productImage;
        RecyclerView productColors;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.promotion_img);
            productColors = itemView.findViewById(R.id.colors);
            productName = itemView.findViewById(R.id.name_product);
            productMaterials = itemView.findViewById(R.id.materials);
            productPrice = itemView.findViewById(R.id.price);
        }

        public void setProductName(String productName) {
            this.productName.setText(productName);
        }


        public void setProductPrice(String productPrice) {
            this.productPrice.setText(productPrice);
        }

        public void setProductMaterials(List<String> productMaterials) {
            this.productMaterials.setText(productMaterials.toString().substring(1, productMaterials.toString().length() - 1));
        }

        public void setProductImage(String productImage) {

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(100, 100);
            Glide.with(context).load(Uri.parse(productImage)).apply(requestOptions).into(this.productImage);

        }

        public void setProductColors(List<String> productColors) {
            ColorsAdapter ada = new ColorsAdapter(productColors);
            this.productColors.setHasFixedSize(true);
            this.productColors.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            this.productColors.setAdapter(ada);
        }
    }

}