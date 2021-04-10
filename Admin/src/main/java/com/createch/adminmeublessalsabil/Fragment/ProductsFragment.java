package com.createch.adminmeublessalsabil.Fragment;

import android.content.Intent;
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

import com.createch.adminmeublessalsabil.Activity.AddProduct;
import com.createch.adminmeublessalsabil.Adapter.ProductsAdapter;
import com.createch.adminmeublessalsabil.Model.Item;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProductsFragment extends Fragment {

    ProductsAdapter adapter;
    CollectionReference ref;
    FirestoreRecyclerOptions<Item> options;

    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false);
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


        view.findViewById(R.id.gotocollections).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to Collections .....
            }
        });
        view.findViewById(R.id.addproduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to add product .....
                Intent intent = new Intent(getContext(), AddProduct.class);
                startActivity(intent);
            }
        });

    }

    private void SearchFor(String field) {
        Query query1 = ref.whereGreaterThanOrEqualTo("name", field).
                whereLessThanOrEqualTo("name", field + "\uf8ff");
        FirestoreRecyclerOptions<Item> options1 =
                new FirestoreRecyclerOptions.Builder<Item>()
                        .setQuery(query1, Item.class).build();
        adapter.updateOptions(options1);


    }

    private void setUpRecyclerView() {
        ref = FirebaseFirestore.getInstance().collection("Products");

        Query query = ref;
        options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        adapter = new ProductsAdapter(options, getContext());

        RecyclerView recyclerView = getView().findViewById(R.id.prod_recy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

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