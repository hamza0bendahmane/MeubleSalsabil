package com.createch.meublessalsabil.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.meublessalsabil.Activity.Notifications;
import com.createch.meublessalsabil.Adapter.FavouritesAdapter;
import com.createch.meublessalsabil.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavouritesFragment extends Fragment {
    FavouritesAdapter prodadapter;
    FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favourites, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        fetchFavorites(getView());
        root.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Notifications.class));
            }
        });
        root.findViewById(R.id.call_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, new CallUsFragment())
                        .commit();
            }
        });

    }

    void fetchFavorites(View root) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Temporary").child("Favorites").
                child(thisUser.getUid());
        FirebaseRecyclerOptions<String> options =
                new FirebaseRecyclerOptions.Builder<String>()
                        .setQuery(ref, String.class)
                        .build();
        prodadapter = new FavouritesAdapter(options, getContext());
        RecyclerView recyclerView = root.findViewById(R.id.fav_recy);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(prodadapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        prodadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        prodadapter.stopListening();
    }

}