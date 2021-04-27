package com.createch.meublessalsabil.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.meublessalsabil.Activity.Notifications;
import com.createch.meublessalsabil.Adapter.ShopListAdapter;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Soldable;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ShopListFragment extends Fragment {
    ShopListAdapter adapter;
    FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_shoplist, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        fetchShopList(getView());
        root.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Notifications.class));
            }
        });
        root.findViewById(R.id.orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, new OrdersFragment())
                        .commit();
            }
        });

    }

    void fetchShopList(View root) {
        CollectionReference ref = FirebaseFirestore.getInstance()
                .collection("Temporary").document("ShopList").
                        collection(thisUser.getUid());
        Query query = ref;
        FirestoreRecyclerOptions<Soldable> options;
        options = new FirestoreRecyclerOptions.Builder<Soldable>()
                .setQuery(query, Soldable.class)
                .build();
        adapter = new ShopListAdapter(options, getContext());
        recyclerView = root.findViewById(R.id.nestedScrollView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        setTotal();
        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                setTotal();
            }
        });
    }

    private void setTotal() {
        double price = 0.0;
        adapter.notifyDataSetChanged();
        TextView total = getView().findViewById(R.id.total);
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            adapter.notifyItemChanged(i);
            Soldable soldi = adapter.getItem(i);
            TextView vv = recyclerView.getChildAt(i).findViewById(R.id.price);
            Log.d("hbhb", "setTotal: price" + price);

            price = price + (Double.valueOf(vv.getText().toString()) * soldi.getQuantity());
            Log.d("hbhb", "quant: " + soldi.getQuantity());
            Log.d("hbhb", "quant: " + Double.valueOf(vv.getText().toString()));

        }
        total.setText(String.valueOf(price));
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        setTotal();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTotal();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}