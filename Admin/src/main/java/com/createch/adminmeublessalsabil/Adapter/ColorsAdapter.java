package com.createch.adminmeublessalsabil.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.adminmeublessalsabil.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorHolder> {
    List<String> colors;
    String tag;
    String color;

    public ColorsAdapter(List<String> colors, String tag, String color) {
        this.colors = colors;
        this.tag = tag;
        this.color = color;
    }


    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_color,
                parent, false);
        return new ColorHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
        holder.image.setCardBackgroundColor(Color.parseColor(colors.get(position)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color = colors.get(position);
            }
        });
        if (tag.equals("remove"))
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    removeAt(position);

                    return false;
                }
            });
    }


    @Override
    public int getItemCount() {
        return colors.size();
    }

    public String getColor() {
        return color;
    }

    public void removeAt(int position) {
        colors.remove(position);
        notifyDataSetChanged();
    }

    class ColorHolder extends RecyclerView.ViewHolder {
        MaterialCardView image;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.color_c);

        }




    }
}
