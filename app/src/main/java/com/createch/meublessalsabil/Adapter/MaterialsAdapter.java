package com.createch.meublessalsabil.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.meublessalsabil.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;


public class MaterialsAdapter extends RecyclerView.Adapter<MaterialsAdapter.MaterialHolder> {
    public int choosed_position = -1;
    List<String> materials;
    Context context;

    public MaterialsAdapter(Context context, List<String> materials) {
        this.materials = materials;
        this.context = context;
    }


    @NonNull
    @Override
    public MaterialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_background,
                parent, false);
        return new MaterialHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialHolder holder, int position) {


        holder.material_text.setText(materials.get(position));
        if (choosed_position == position) {
            holder.background.setCardBackgroundColor(Color.parseColor("#000000"));
            holder.material_text.setTextColor(Color.WHITE);
        } else {
            holder.background.setCardBackgroundColor(Color.parseColor("#ffffff"));
            holder.material_text.setTextColor(Color.BLACK);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosed_position = position;
                notifyDataSetChanged();


            }
        });

    }


    @Override
    public int getItemCount() {
        return materials.size();
    }


    class MaterialHolder extends RecyclerView.ViewHolder {
        MaterialCardView background;
        TextView material_text;


        public MaterialHolder(@NonNull View itemView) {
            super(itemView);
            material_text = itemView.findViewById(R.id.material_text);
            background = itemView.findViewById(R.id.background);

        }


    }
}
