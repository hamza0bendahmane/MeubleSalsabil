package com.createch.meublessalsabil.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.createch.meublessalsabil.models.Order;
import com.createch.meublessalsabil.models.Soldable;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class ShopListFragment extends Fragment {
    ShopListAdapter adapter;
    MaterialButton confirmer_acht;
    FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView recyclerView;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Temporary").child("ShopList").
            child(thisUser.getUid());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_shoplist, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        confirmer_acht = root.findViewById(R.id.confirmer_acht);
        fetchShopList(root);
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

        confirmer_acht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double tot = setTotal(root);
                // push order .....
                HashMap<String, Object> map = new HashMap<>();
                HashMap<String, Long> date = new HashMap<>();

                map.put("date", date.put(Order.WAITING, System.currentTimeMillis()));
                map.put("userId", thisUser.getUid());
                map.put("state", Order.WAITING);
                map.put("totalPrice", tot);

                DatabaseReference pushed = FirebaseDatabase.getInstance().getReference().child("PushedOrders").push();
                ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        pushed.setValue(dataSnapshot.getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                map.put("soldItems", pushed.toString());
                                FirebaseFirestore.getInstance().collection("Orders").document(UUID.randomUUID().toString())
                                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ref.removeValue();
                                    }
                                });

                            }
                        });
                    }
                });


            }
        });
    }


    void fetchShopList(View root) {

        FirebaseRecyclerOptions<Soldable> options;
        options = new FirebaseRecyclerOptions.Builder<Soldable>()
                .setQuery(ref, Soldable.class)
                .build();
        adapter = new ShopListAdapter(options, getContext(), true);
        recyclerView = root.findViewById(R.id.orders_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        setTotal(root);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setTotal(root);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public double setTotal(View v) {
        double price = 0.0;
        if (isVisible()) {
            adapter.notifyDataSetChanged();
            TextView total = v.findViewById(R.id.total);
            if (adapter.getItemCount() != 0)
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    adapter.notifyItemChanged(i);
                    Soldable soldi = adapter.getItem(i);
                    TextView vv = recyclerView.getChildAt(i).findViewById(R.id.price);
                    Log.d("hbhb", "setTotal: price" + price);

                    price = price + (Double.valueOf(vv.getText().toString()) * soldi.getQuantity());
                    Log.d("hbhb", "quant: " + soldi.getQuantity());
                    Log.d("hbhb", "quant: " + Double.valueOf(vv.getText().toString()));

                }
            AnimateTextView(total, price);
        }
        return price;

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

    void AnimateTextView(TextView priceTextView, Double price_text) {
        double from = 0.0;

        for (double i = from; i <= price_text; i += 10.0) {
            double finalI = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    priceTextView.setText(String.valueOf(finalI));

                }
            }, 400);

        }

    }
}