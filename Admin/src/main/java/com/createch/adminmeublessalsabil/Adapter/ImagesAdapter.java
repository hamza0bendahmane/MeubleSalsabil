package com.createch.adminmeublessalsabil.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.adminmeublessalsabil.R;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageHolder> {


    List<String> colors;
    String tag ;

    public ImagesAdapter(List<String> colors , String tag) {
        this.colors = colors;
        this.tag = tag;
    }


    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_hold,
                parent, false);
        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {






        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(100, 100) ;
        Glide.with(holder.itemView.getContext()).load(Uri.parse(colors.get(position))).apply(requestOptions).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return colors.size();
    }


    class ImageHolder extends RecyclerView.ViewHolder  {
        ImageView image;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_id);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(tag.equals("remove")){
                        removeAt(getAdapterPosition());
                    }
                    return false;
                }
            });
        }




        public void removeAt(int position) {
            colors.remove(position);
            notifyDataSetChanged();
        }
    }
}
