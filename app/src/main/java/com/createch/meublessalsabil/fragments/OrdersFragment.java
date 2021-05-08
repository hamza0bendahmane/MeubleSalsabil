package com.createch.meublessalsabil.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.meublessalsabil.Adapter.OrdersAdapter;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Order;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    OrdersAdapter adapter;
    FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView recyclerView;

    ArrayList<String> soldRefs = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        fetchOrders(root);


    }


    void fetchOrders(View root) {
        CollectionReference ref = FirebaseFirestore.getInstance().collection("Orders");
        Query query = ref.whereEqualTo("userId", thisUser.getUid()).orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Order> options;
        options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();
        adapter = new OrdersAdapter(options, getContext());
        recyclerView = root.findViewById(R.id.orders_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

}
