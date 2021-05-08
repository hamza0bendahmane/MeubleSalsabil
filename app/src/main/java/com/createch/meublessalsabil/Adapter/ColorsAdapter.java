package com.createch.meublessalsabil.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.meublessalsabil.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorHolder> {
    List<String> colors;
    public int choosed_position = -1;
    boolean selectable;
    Context context;

    public ColorsAdapter(Context context, List<String> colors, boolean selectable) {
        this.colors = colors;
        this.context = context;
        this.selectable = selectable;

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
        if (selectable) {

            if (choosed_position == position)
                holder.itemView.setBackgroundColor(Color.parseColor("#000000"));
            else
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    choosed_position = position;
                    notifyDataSetChanged();


                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return colors.size();
    }


    class ColorHolder extends RecyclerView.ViewHolder {
        MaterialCardView image;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.color_c);
        }


    }
}
