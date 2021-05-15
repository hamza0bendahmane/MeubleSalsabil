package com.createch.adminmeublessalsabil.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.adminmeublessalsabil.Adapter.UsersAdapter;
import com.createch.adminmeublessalsabil.Model.User;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class UserFragment extends Fragment {
    UsersAdapter adapter;
    CollectionReference ref;
    FirestoreRecyclerOptions<User> options;

    public UserFragment() {
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BottomNavigationView bottom_navigation = getActivity().findViewById(R.id.nav_view);
        if (!bottom_navigation.isShown())
            bottom_navigation.setVisibility(View.VISIBLE);
        setUpRecyclerView();
        SearchView ss = view.findViewById(R.id.simpleSearchView);
        view.findViewById(R.id.gotoaccepted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ff = new BlockedUsersFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, ff)
                        .commit();
            }
        });
        ss.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String s = newText;
                if (!s.trim().isEmpty())
                    SearchFor(s.trim());
                else
                    adapter.updateOptions(options);
                return false;
            }
        });


    }

    private void SearchFor(String field) {
        Query query1 = ref.whereEqualTo("blocked", false).whereGreaterThanOrEqualTo("fname", field).
                whereLessThanOrEqualTo("fname", field + "\uf8ff");
        FirestoreRecyclerOptions<User> options1 =
                new FirestoreRecyclerOptions.Builder<User>()
                        .setQuery(query1, User.class).build();
        adapter.updateOptions(options1);


    }

    private void setUpRecyclerView() {
        ref = FirebaseFirestore.getInstance().collection("Users");

        Query query = ref.whereEqualTo("blocked", false);
        options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        adapter = new UsersAdapter(options, getContext());

        RecyclerView recyclerView = getView().findViewById(R.id.user_recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        // Set the adapter
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}