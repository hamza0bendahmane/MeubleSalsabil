package com.createch.adminmeublessalsabil.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.adminmeublessalsabil.Adapter.ColorsAdapter;
import com.createch.adminmeublessalsabil.Adapter.ImagesAdapter;
import com.createch.adminmeublessalsabil.Adapter.ProductsAdapter;
import com.createch.adminmeublessalsabil.Model.Item;
import com.createch.adminmeublessalsabil.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EditCollection extends AppCompatActivity {

    public ArrayList<Item> selected_items = new ArrayList<>();
    ProductsAdapter adapter ;
    ColorsAdapter colorsadapter;
    ImagesAdapter ada ;
    int successCount = 0;
    List<String> selected_images,selected_uris,selected_colors,selected_materials,selected_contents ;
    MaterialButton addProduct,cancel , submit,addImage;
    TextInputEditText description , quantity,price,name;
    RecyclerView products ,images ,colors_rec;
    String refer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_collection);
        refer = getIntent().getStringExtra("ref");

        selected_uris = new ArrayList<>();
        selected_images = new ArrayList<String>();
        selected_colors =new ArrayList<String>();
        selected_materials =new ArrayList<String>();
        selected_contents =new ArrayList<String>();



        colors_rec =findViewById(R.id.colors_rec);
        products = findViewById(R.id.pro_picked);
        description = findViewById(R.id.width_produ);
        addProduct = findViewById(R.id.add_material);
        cancel = findViewById(R.id.cancel_name);
        submit = findViewById(R.id.submit_name);
        addImage = findViewById(R.id.add_pic);
        price = findViewById(R.id.price_produ);
        quantity = findViewById(R.id.quant_produ);
        name = findViewById(R.id.name_prod);
        images = findViewById(R.id.pro_pic);
        fillTheInfos();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushChanges();
            }
        });
       // setupRecyclerImages();
       // setUpRecyclerProducts();

    }

    private void pushChanges() {



    }

    public void cancel(View vv){

        onBackPressed();
    }

    public void pick_image(View vv) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null && data.getData() != null) {
            selected_images.add(data.getData().toString());
            ada.notifyDataSetChanged();
        }
    }

    private void fillTheInfos() {
        Log.d("hbhb", "fillTheInfos: id ref "+refer);
        FirebaseFirestore.getInstance().collection("Collections").document(refer)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                selected_images = (List<String>) documentSnapshot.get("images");
                selected_materials = (List<String>) documentSnapshot.get("materials");
                selected_colors = (ArrayList<String>) documentSnapshot.get("colors");
                name.setText(documentSnapshot.get("name").toString());
                price.setText(documentSnapshot.get("price").toString());
                quantity.setText(documentSnapshot.get("quantity").toString());


                // image ....

                // colors
                colorsadapter = new ColorsAdapter(selected_colors,"remove");
                colors_rec.setHasFixedSize(true);
                colors_rec.setLayoutManager(new GridLayoutManager(getApplicationContext(),5));
                colors_rec.setAdapter(adapter);




                // materials ..
                ada = new ImagesAdapter(selected_images,"remove");
                images.setHasFixedSize(true);
                images.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                images.setAdapter(ada);
                // spinner ...


            }
        });
    }


}