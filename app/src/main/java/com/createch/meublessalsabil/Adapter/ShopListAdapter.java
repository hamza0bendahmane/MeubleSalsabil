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
import com.createch.meublessalsabil.fragments.ProductShow;
import com.createch.meublessalsabil.models.Item;
import com.createch.meublessalsabil.models.Promotion;
import com.createch.meublessalsabil.models.Soldable;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShopListAdapter extends FirebaseRecyclerAdapter<Soldable, ShopListAdapter.ProductHolder> {
    Context context;
    Promotion thisPromotion = null;
    Item mode;
    boolean editable;

    public ShopListAdapter(@NonNull FirebaseRecyclerOptions<Soldable> options, Context context, boolean editable) {
        super(options);
        this.context = context;
        this.editable = editable;
    }


    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Soldable model) {
        if (!editable) {
            holder.plus.setVisibility(View.GONE);
            holder.minus.setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.delete).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.quanto).setVisibility(View.VISIBLE);

        }
        String refer = getSnapshots().getSnapshot(position).getKey();
        holder.setChanges(refer);
        holder.setProductColor(model.getColor());
        holder.setProductMaterial(model.getMaterial());
        holder.setProductQuantity(String.valueOf(model.getQuantity()));
        FirebaseFirestore.getInstance().collection("Products")
                .document(refer).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mode = documentSnapshot.toObject(Item.class);

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Bundle bundle = new Bundle();
                bundle.putString("ref", getSnapshots().getSnapshot(position).getKey());
                bundle.putString("promotion", mode.getPromotion());
                Fragment fragment = new ProductShow();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }
        });
        holder.itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getSnapshots().getSnapshot(position).getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                if (model.getQuantity() > 1) {
                    holder.itemView.findViewById(R.id.minus).setEnabled(false);
                    int newone = model.getQuantity() - 1;
                    getSnapshots().getSnapshot(position).getRef().child("quantity").setValue(newone).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(v, "تم  ", Snackbar.LENGTH_SHORT).show();
                            holder.itemView.findViewById(R.id.minus).setEnabled(true);
// make the shoplist item id sameas product id to avoid redu^lic    ate

                        }
                    });
                }
            }
        });
        holder.itemView.findViewById(R.id.plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.findViewById(R.id.plus).setEnabled(false);
                int newone = model.getQuantity() + 1;

                getSnapshots().getSnapshot(position).getRef().child("quantity").setValue(newone).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(v, "تم  ", Snackbar.LENGTH_SHORT).show();
                        holder.itemView.findViewById(R.id.plus).setEnabled(true);

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
            categ = SoldableView.findViewById(R.id.quantity_prod);
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

        public void setChanges(String refer) {


            FirebaseFirestore.getInstance().collection("Products").document(refer).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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