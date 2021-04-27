package com.createch.meublessalsabil.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Item;
import com.createch.meublessalsabil.models.Promotion;
import com.createch.meublessalsabil.models.Soldable;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShopListAdapter extends FirestoreRecyclerAdapter<Soldable, ShopListAdapter.ProductHolder> {
    Context context;
    Promotion thisPromotion = null;

    public ShopListAdapter(@NonNull FirestoreRecyclerOptions<Soldable> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Soldable model) {

        holder.setChanges(model);
        holder.setProductColor(model.getColor());
        holder.setProductMaterial(model.getMaterial());
        holder.setProductQuantity(String.valueOf(model.getQuantity()));

        //    holder.setProductNumber(model.getNumber());
        holder.itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getSnapshots().getSnapshot(position).getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(v, "تم الحذف ", Snackbar.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });
            }
        });
        holder.itemView.findViewById(R.id.minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.findViewById(R.id.minus).setEnabled(false);
                int newone = model.getQuantity() - 1;
                getSnapshots().getSnapshot(position).getReference().update("quantity", newone).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(v, "تم  ", Snackbar.LENGTH_SHORT).show();
                        holder.itemView.findViewById(R.id.minus).setEnabled(true);
                        notifyDataSetChanged();
                    }
                });
            }
        });
        holder.itemView.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.findViewById(R.id.plus).setEnabled(false);
                int newone = model.getQuantity() + 1;

                getSnapshots().getSnapshot(position).getReference().update("quantity", newone).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(v, "تم  ", Snackbar.LENGTH_SHORT).show();
                        holder.itemView.findViewById(R.id.plus).setEnabled(true);
                        notifyDataSetChanged();
// make the shoplist item id sameas product id to avoid redu^lic    ate

                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.soldable_layout,
                parent, false);
        return new ProductHolder(v);
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView categ;
        TextView productNumber;
        MaterialCardView image_color;
        ImageView plus, minus;
        TextView productMaterials;
        ImageView productImage;


        public ProductHolder(@NonNull View SoldableView) {
            super(SoldableView);
            productImage = SoldableView.findViewById(R.id.pimg);
            image_color = itemView.findViewById(R.id.color_c);
            productName = SoldableView.findViewById(R.id.namecatroduct);
            productNumber = SoldableView.findViewById(R.id.number_of_sold);
            plus = SoldableView.findViewById(R.id.plus);
            categ = SoldableView.findViewById(R.id.category_product);
            minus = SoldableView.findViewById(R.id.minus);
            productMaterials = SoldableView.findViewById(R.id.materia);
            productPrice = SoldableView.findViewById(R.id.price);
        }


        public void setProductColor(String productColor) {
            image_color.setCardBackgroundColor(Color.parseColor(productColor));

        }

        public void setProductMaterial(String m) {
            productMaterials.setText(m);

        }

        public void setProductQuantity(String q) {
            productNumber.setText(q);

        }

        public void setChanges(Soldable model) {


            FirebaseFirestore.getInstance().collection("Products").document(model.getProduct_id()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapsho) {
                    Item item = documentSnapsho.toObject(Item.class);
                    if (!item.getPromotion().isEmpty())
                        FirebaseFirestore.getInstance().collection("Promotions").document(item.getPromotion()).get().
                                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                                        thisPromotion = documentSnapshot.toObject(Promotion.class);
                                        double price_discounted = 0.0;
                                        if (thisPromotion != null) {
                                            price_discounted = item.getPrice() * thisPromotion.getDiscount() / 100;

                                        }
                                        double finalPrice = item.getPrice() - price_discounted;
                                        productPrice.setText(String.valueOf(finalPrice));


                                    }
                                });
                    else {
                        // no promotion ...
                        productPrice.setText(String.valueOf(item.getPrice()));

                    }

                    productName.setText(item.getName());
                    categ.setText(item.getCategory());
                    RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter();
                    Glide.with(context).load(Uri.parse(item.getImage())).apply(requestOptions).
                            into(productImage);

                }
            });
        }
    }

}