package com.createch.adminmeublessalsabil.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.adminmeublessalsabil.Activity.EditProduct;
import com.createch.adminmeublessalsabil.Adapter.ColorsAdapter;
import com.createch.adminmeublessalsabil.Model.Item;
import com.createch.adminmeublessalsabil.Model.Promotion;
import com.createch.adminmeublessalsabil.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductShow extends Fragment {
    String ref, prom_ref;
    Item model;
    ScrollView scrollView;
    Promotion prom_model;
    MaterialButton edit_product, delete_product;
    TextView productName, discount, endate, productPrice, productHeight, productQuantity, productWidth, productMaterials, productCategory, productLength;
    ImageView productImage;
    RecyclerView productColors;
    double finalPrice;


    public ProductShow() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_show, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle b = getArguments();
        View itemView = getView();
        View promotionView = itemView.findViewById(R.id.promotion_layout);
        productImage = itemView.findViewById(R.id.image_product);
        productColors = itemView.findViewById(R.id.colors);
        scrollView = view.findViewById(R.id.scroler);
        productCategory = itemView.findViewById(R.id.quantity_prod);
        productHeight = itemView.findViewById(R.id.height);
        productName = itemView.findViewById(R.id.name_product);
        productLength = itemView.findViewById(R.id.length);
        productQuantity = itemView.findViewById(R.id.quantity);
        productMaterials = itemView.findViewById(R.id.materials);
        productPrice = itemView.findViewById(R.id.price);
        productWidth = itemView.findViewById(R.id.width);
        discount = itemView.findViewById(R.id.promotion);
        endate = itemView.findViewById(R.id.phone_moderator);

        if (b != null) {
            ref = b.getString("ref");
            prom_ref = b.getString("promotion");

        } else
            getActivity().onBackPressed();
        edit_product = view.findViewById(R.id.edit_product);
        delete_product = view.findViewById(R.id.delete_product);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int dyy, int oldScrollX, int dy) {
                BottomNavigationView bottom_navigation = getActivity().findViewById(R.id.nav_view);
                if (dy > 0 && bottom_navigation.isShown()) {
                    bottom_navigation.setVisibility(View.GONE);
                } else if (dy < 0) {
                    bottom_navigation.setVisibility(View.VISIBLE);

                }
            }
        });

        FirebaseFirestore.getInstance().collection("Products")
                .document(ref).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                model = documentSnapshot.toObject(Item.class);
                finalPrice = model.getPrice();
                if (!prom_ref.isEmpty()) {
                    promotionView.setVisibility(View.VISIBLE);
                    FirebaseFirestore.getInstance().collection("Promotions")
                            .document(prom_ref).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            prom_model = documentSnapshot.toObject(Promotion.class);
                            updatePromtionView(String.valueOf(prom_model.getDiscount()), prom_model.getEndDate());
                            if (prom_model != null) {
                                finalPrice = model.getPrice() - prom_model.getDiscount() * model.getPrice() / 100;
                            }
                        }
                    });
                }

                setProductCategory(model.getCategory());
                setProductColors(model.getColors());
                setProductHeight(String.valueOf(model.getHeight()));
                setProductImage(model.getImage());
                setProductLength(String.valueOf(model.getLength()));
                setProductMaterials(model.getMaterials());
                setProductName(model.getName());
                setProductPrice(String.valueOf(model.getPrice()));
                setProductQuantity(String.valueOf(model.getQuantity()));
                setProductWidth(String.valueOf(model.getWidth()));
            }
        });

        edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProduct.class).putExtra("ref",ref));
            }
        });
        delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setMessage("Are u sure want to delete this product ?");
                dialog.setButton(Dialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore.getInstance().collection("Products")
                                .document(ref).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                getActivity().onBackPressed();

                            }
                        });
                    }
                });
                dialog.setButton(Dialog.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    private void updatePromtionView(String discountpercentage, String date) {

        discount.setText(getContext().getString(R.string.discount) + " " + discountpercentage + " %");
        endate.setText(date);
    }


    public void setProductName(String productNamee) {
        productName.setText(productNamee);
    }

    public void setProductQuantity(String productQuantitye) {
        productQuantity.setText(new StringBuilder().append(getString(R.string.quantity)).
                append(productQuantitye).toString());
    }

    public void setProductHeight(String productHeighte) {
        productHeight.setText(getString(R.string.height) + productHeighte);
    }

    public void setProductLength(String productLengthe) {
        productLength.setText(getString(R.string.length) + productLengthe);
    }

    public void setProductWidth(String productWidthe) {
        productWidth.setText(getString(R.string.width) + productWidthe);
    }

    public void setProductPrice(String productPricee) {
        productPrice.setText(productPricee + " " + getActivity().getResources().getString(R.string.currency));
    }

    public void setProductCategory(String productCategorye) {
        productCategory.setText(getString(R.string.category) + productCategorye);
    }

    public void setProductMaterials(List<String> productMaterialse) {
        productMaterials.setText(productMaterialse.toString().substring(1, productMaterialse.toString().length() - 1));
    }

    public void setProductImage(String productImagee) {

        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter();

        Glide.with(getContext()).load(Uri.parse(productImagee)).apply(requestOptions).into(productImage);

    }

    public void setProductColors(List<String> productColorse) {
        ColorsAdapter ada = new ColorsAdapter(productColorse, "", "");
        productColors.setHasFixedSize(true);
        productColors.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        productColors.setAdapter(ada);
    }

}