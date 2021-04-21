package com.createch.meublessalsabil.Adapter;

import android.content.Context;
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
import com.createch.meublessalsabil.fragments.ProductShow;
import com.createch.meublessalsabil.models.Promotion;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PromotionsAdapter extends FirestoreRecyclerAdapter<Promotion, PromotionsAdapter.PromotionHolder> {

    Context context;

    public PromotionsAdapter(@NonNull FirestoreRecyclerOptions<Promotion> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PromotionHolder holder, int position, @NonNull Promotion model) {
        holder.setProductImage(model.getImage());
        holder.setDiscount(String.valueOf(model.getDiscount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Bundle bundle = new Bundle();
                bundle.putString("ref", model.getProductId());
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
    public PromotionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_layout,
                parent, false);
        return new PromotionHolder(v);
    }

    public class PromotionHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView discount;

        public PromotionHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.promotion_img);
            discount = itemView.findViewById(R.id.delete);

        }

        public void setDiscount(String discountpercentage) {
            discount.setText(context.getString(R.string.discount) + " " + discountpercentage + " %");
        }

        public void setProductImage(String im) {

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(100, 100);
            Glide.with(context).load(im).apply(requestOptions).into(this.productImage);

        }


    }
}
