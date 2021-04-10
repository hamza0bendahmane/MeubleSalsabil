package com.createch.adminmeublessalsabil.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.adminmeublessalsabil.Model.Order;
import com.createch.adminmeublessalsabil.R;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    // creating variables for our ArrayList and context
    private final ArrayList<Order> OrderArrayList;
    private final Context context;

    // creating constructor for our adapter class
    public OrdersAdapter(ArrayList<Order> OrderArrayList, Context context) {
        this.OrderArrayList = OrderArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Order Order = OrderArrayList.get(position);
        holder.courseNameTV.setText(Integer.toString(Order.getTotalPrice()));
        holder.courseDurationTV.setText(Order.getDate().toString());
        holder.courseDescTV.setText(Order.getState());
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return OrderArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView courseNameTV;
        private final TextView courseDurationTV;
        private final TextView courseDescTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            courseDurationTV = itemView.findViewById(R.id.idTVCourseDuration);
            courseDescTV = itemView.findViewById(R.id.idTVCourseDescription);
        }
    }


}