package com.createch.adminmeublessalsabil.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.adminmeublessalsabil.Activity.EditProduct;
import com.createch.adminmeublessalsabil.Model.Item;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends FirestoreRecyclerAdapter<Item, ProductsAdapter.ProductHolder> {
    Context context;

    public ProductsAdapter(@NonNull FirestoreRecyclerOptions<Item> options, Context context) {
        super(options);
        this.context = context;
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

        holder.itemView.findViewById(R.id.edit_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(context, EditProduct.class);
                ii.putExtra("ref", getSnapshots().getSnapshot(position).getId());
                Log.d("hbhb", "onClick: " + getSnapshots().getSnapshot(position).getId());
                context.startActivity(ii);
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


        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.image_product);
            productColors = itemView.findViewById(R.id.colors);
            productCategory = itemView.findViewById(R.id.category_product);
            productHeight = itemView.findViewById(R.id.height);
            productName = itemView.findViewById(R.id.name_product);
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
            Picasso.get().load(productImage).fit().centerCrop().into(this.productImage);
        }

        public void setProductColors(List<String> productColors) {
            ColorsAdapter ada = new ColorsAdapter(productColors);
            this.productColors.setHasFixedSize(true);
            this.productColors.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            this.productColors.setAdapter(ada);
        }
    }

}
