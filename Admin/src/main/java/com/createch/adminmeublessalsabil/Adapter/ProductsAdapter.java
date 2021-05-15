package com.createch.adminmeublessalsabil.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.createch.adminmeublessalsabil.Activity.EditProduct;
import com.createch.adminmeublessalsabil.Fragment.ProductShow;
import com.createch.adminmeublessalsabil.Model.Item;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends FirestoreRecyclerAdapter<Item, ProductsAdapter.ProductHolder> {
    Context context;

    ArrayList<Item> selected_items ;
    String tag = "";
    public ProductsAdapter(@NonNull FirestoreRecyclerOptions<Item> options, Context context, String tag , ArrayList<Item> selected_items) {
        super(options);
        this.context = context;
        this.tag = tag;
        this.selected_items = selected_items;
    }

    public ArrayList<Item> getSelected_items() {
        return selected_items;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Item model) {
        holder.setProductCategory(model.getCategory());
        holder.setProductColors(model.getColors());
        holder.setProductHeight(String.valueOf(model.getHeight()));
        holder.setProductImage(model.getImage());
        holder.setProductLength(String.valueOf(model.getLength()));
        holder.setProductMaterials(model.getMaterials());
        holder.setProductName(model.getName());
        holder.setProductPrice(String.valueOf(model.getPrice()));
        holder.setProductQuantity(String.valueOf(model.getQuantity()));
        holder.setProductWidth(String.valueOf(model.getWidth()));
        if (tag.equals("remove")){
            holder.checkBox.setVisibility(View.VISIBLE);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        selected_items.add(model);

                    }else{

                        selected_items.remove(model);
                    }


            }
        });


        }
        holder.itemView.findViewById(R.id.edit_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(context, EditProduct.class);
                ii.putExtra("ref", getSnapshots().getSnapshot(position).getId());
                Log.d("hbhb", "onClick: " + getSnapshots().getSnapshot(position).getId());
                context.startActivity(ii);
            }
        });
        holder.itemView.findViewById(R.id.name_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Bundle bundle = new Bundle();
                bundle.putString("ref", getSnapshots().getSnapshot(position).getId());
                bundle.putString("promotion", model.getPromotion());
                Fragment fragment = new ProductShow();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.nav_host_fragment, fragment).addToBackStack("app")
                        .commit();
            }
        });
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,
                parent, false);
        return new ProductHolder(v);
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productQuantity;
        TextView productHeight;
        TextView productLength;
        TextView productWidth;
        TextView productPrice;
        TextView productCategory;
        TextView productMaterials;
        ImageView productImage;
        RecyclerView productColors;
        CheckBox checkBox ;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image_product);
            productColors = itemView.findViewById(R.id.colors);
            productCategory = itemView.findViewById(R.id.category_product);
            productHeight = itemView.findViewById(R.id.height);
            productName = itemView.findViewById(R.id.name_product);
            checkBox = itemView.findViewById(R.id.chec_box);
            productLength = itemView.findViewById(R.id.length);
            productQuantity = itemView.findViewById(R.id.quantity);
            productMaterials = itemView.findViewById(R.id.materials);
            productPrice = itemView.findViewById(R.id.price);
            productWidth = itemView.findViewById(R.id.width);
        }

        public void setProductName(String productName) {
            this.productName.setText(productName);
        }

        public void setProductQuantity(String productQuantity) {
            this.productQuantity.setText(new StringBuilder().append(context.getString(R.string.quantity)).append(productQuantity).toString());
        }

        public void setProductHeight(String productHeight) {
            this.productHeight.setText(context.getString(R.string.height) + productHeight);
        }

        public void setProductLength(String productLength) {
            this.productLength.setText(context.getString(R.string.length) + productLength);
        }

        public void setProductWidth(String productWidth) {
            this.productWidth.setText(context.getString(R.string.width) + productWidth);
        }

        public void setProductPrice(String productPrice) {
            this.productPrice.setText(productPrice);
        }

        public void setProductCategory(String productCategory) {
            this.productCategory.setText(context.getString(R.string.category) + productCategory);
        }

        public void setProductMaterials(List<String> productMaterials) {
            this.productMaterials.setText(productMaterials.toString().substring(1, productMaterials.toString().length() - 1));
        }

        public void setProductImage(String productImage) {

            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(100, 100) ;
            Glide.with(context).load(Uri.parse(productImage)).apply(requestOptions).into(this.productImage);

        }

        public void setProductColors(List<String> productColors) {
            ColorsAdapter ada = new ColorsAdapter(productColors, "", "");
            this.productColors.setHasFixedSize(true);
            this.productColors.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            this.productColors.setAdapter(ada);
        }
    }

}
