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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.meublessalsabil.Activity.Notifications;
import com.createch.meublessalsabil.Adapter.ShopListAdapter;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Soldable;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                if (adapter.getItemCount() != 0) {
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    Bundle bundle = new Bundle();
                    bundle.putString("ref", String.valueOf(ref));
                    bundle.putDouble("tot", setTotal(root));
                    Fragment fragment = new ConfirmShopingList();
                    fragment.setArguments(bundle);
                    getParentFragmentManager().executePendingTransactions();
                    getParentFragmentManager().beginTransaction().
                            replace(R.id.nav_host_fragment, fragment).addToBackStack("app")
                            .commit();
                } else
                    Snackbar.make(confirmer_acht, "Shouldnt be empty", Snackbar.LENGTH_SHORT).show();

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
                    adapter.notifyDataSetChanged();
                    Soldable soldi = adapter.getItem(i);
                    price = price + soldi.getPrice() * soldi.getQuantity();

                    Log.d("hbhb", "setTotal: price" + price);
                    Log.d("hbhb", "quant: " + soldi.getQuantity());

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