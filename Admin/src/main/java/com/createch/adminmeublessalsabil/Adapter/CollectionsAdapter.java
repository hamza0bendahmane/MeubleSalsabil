package com.createch.adminmeublessalsabil.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.adminmeublessalsabil.Activity.EditCollection;
import com.createch.adminmeublessalsabil.Model.Model;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class CollectionsAdapter  extends FirestoreRecyclerAdapter<Model, CollectionsAdapter.CollectionHolder> {


      Context context;

    public CollectionsAdapter(@NonNull FirestoreRecyclerOptions<Model> options, Context context) {
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public CollectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_layout,
                parent, false);
        return new CollectionHolder(v);
    }


    @Override
    protected void onBindViewHolder(@NonNull CollectionHolder holder, int position, @NonNull Model collection) {
        holder.setcollectionContent(collection.getContents().toString());
        holder.setcollectionColors(collection.getColors());
        holder.setcollectionImages(collection.getImages());
        holder.setcollectionMaterials(collection.getMaterials().toString());
        holder.setcollectionName(collection.getName());
        holder.setcollectionPrice(String.valueOf(collection.getPrice()));
        holder.setcollectionQuantity(String.valueOf(collection.getQuantity()));





        holder.itemView.findViewById(R.id.edit_collection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(context, EditCollection.class);
                ii.putExtra("ref", getSnapshots().getSnapshot(position).getId());
                context.startActivity(ii);
            }
        });
    }


    public class CollectionHolder extends RecyclerView.ViewHolder {
        TextView collectionName;
        TextView collectionQuantity;
        TextView collectionPrice;
        TextView collectionContent;
        TextView collectionMaterials;
        ImageView collectionImage;
        RecyclerView collectionColors;
        RecyclerView images;


        public CollectionHolder(@NonNull View ModelView) {
            super(ModelView);
            collectionImage = ModelView.findViewById(R.id.edit_collection);
            collectionColors = ModelView.findViewById(R.id.colors);
            images = ModelView.findViewById(R.id.images);
            collectionContent = ModelView.findViewById(R.id.content_coll);
            collectionName = ModelView.findViewById(R.id.name_product);
            collectionQuantity = ModelView.findViewById(R.id.qqq);
            collectionMaterials = ModelView.findViewById(R.id.materials);
            collectionPrice = ModelView.findViewById(R.id.price);
        }

        public void setcollectionName(String collectionName) {
            this.collectionName.setText(collectionName);
        }

        public void setcollectionQuantity(String collectionQuantity) {
            this.collectionQuantity.setText(collectionQuantity);
        }
    
        public void setcollectionPrice(String collectionPrice) {
            this.collectionPrice.setText(collectionPrice);
        }

        public void setcollectionContent(String collectionContent) {
            this.collectionContent.setText(collectionContent);
        }

        public void setcollectionMaterials(String collectionMaterials) {
            this.collectionMaterials.setText(collectionMaterials);
        }

        public void setcollectionColors(List<String> collectionColors) {
            ColorsAdapter ada = new ColorsAdapter(collectionColors,"");
            this.collectionColors.setHasFixedSize(true);
            this.collectionColors.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            this.collectionColors.setAdapter(ada);
        }
        public void setcollectionImages(List<String> images) {
            ImagesAdapter adaa = new ImagesAdapter(images,"");
            this.images.setHasFixedSize(true);
            this.images.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            this.images.setAdapter(adaa);
        }
    }
    
    
    
    
}
