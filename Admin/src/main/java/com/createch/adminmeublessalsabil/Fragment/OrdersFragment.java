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

import com.createch.adminmeublessalsabil.Adapter.OrdersAdapter;
import com.createch.adminmeublessalsabil.Model.Order;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OrdersFragment extends Fragment{

    OrdersAdapter adapter;
    CollectionReference ref;
    FirestoreRecyclerOptions<Order> options;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUpRecyclerView();
        SearchView ss = view.findViewById(R.id.simpleSearchView);
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



        view.findViewById(R.id.gotoaccepted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ff = new AcceptedOrders();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, ff)
                        .commit();
            }
        });
        view.findViewById(R.id.gotodelivered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ff = new DeliveredFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, ff)
                        .commit();
            }
        });
    }

    private void SearchFor(String field) {
        Query query1 = ref.whereGreaterThanOrEqualTo("user", field).
                whereLessThanOrEqualTo("user", field + "\uf8ff");
        FirestoreRecyclerOptions<Order> options1 =
                new FirestoreRecyclerOptions.Builder<Order>()
                        .setQuery(query1, Order.class).build();
        adapter.updateOptions(options1);


    }

    private void setUpRecyclerView() {
        ref = FirebaseFirestore.getInstance().collection("Orders");
        Query query = ref.whereEqualTo("state","waiting");
        options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();
        adapter = new OrdersAdapter(options, getContext());

        RecyclerView recyclerView = getView().findViewById(R.id.orders_recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                BottomNavigationView bottom_navigation = getActivity().findViewById(R.id.nav_view);
                if (dy > 0 && bottom_navigation.isShown()) {
                    bottom_navigation.setVisibility(View.GONE);
                } else if (dy < 0 ) {
                    bottom_navigation.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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
