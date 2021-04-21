package com.createch.meublessalsabil.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.fragments.ProductShow;
import com.createch.meublessalsabil.models.Item;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProductsAdapter extends FirestoreRecyclerAdapter<Item, ProductsAdapter.ProductHolder> {

    Context context;

    public ProductsAdapter(@NonNull FirestoreRecyclerOptions<Item> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Item model) {
        holder.setProductImage(model.getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Bundle bundle = new Bundle();
                bundle.putString("ref", getSnapshots().getSnapshot(position).getId());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_featured_card,
                parent, false);
        return new ProductHolder(v);
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        ImageView productImage;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
        }

        public void setProductImage(String productImage) {

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(100, 100);
            Glide.with(context).load(Uri.parse(productImage)).apply(requestOptions).into(this.productImage);

        }


    }
}
