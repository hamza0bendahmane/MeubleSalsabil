package com.createch.adminmeublessalsabil.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.createch.adminmeublessalsabil.Adapter.OrdersAdapter;
import com.createch.adminmeublessalsabil.Model.Order;
import com.createch.adminmeublessalsabil.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView courseRV;
    ArrayList<Order> OrderArrayList;
    OrdersAdapter OrdersAdapter;
    FirebaseFirestore db;
    ProgressBar loadingPB;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        // initializing our variables.
        courseRV = view.findViewById(R.id.orders_recy);
        loadingPB = view.findViewById(R.id.idProgressBar);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mswipe);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        // Fetching data from server
        loadData();

        // creating our new array list

    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        OrderArrayList = new ArrayList<>();
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(new LinearLayoutManager(getContext()));

        // adding our array list to our recycler view adapter class.
        OrdersAdapter = new OrdersAdapter(OrderArrayList, getContext());

        // setting adapter to our recycler view.
        courseRV.setAdapter(OrdersAdapter);

        // below line is use to get the data from Firebase Firestore.
        // previously we were saving data on a reference of Order
        // now we will be getting the data from the same reference.
        db.collection("Orders").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            loadingPB.setVisibility(View.GONE);
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                Order c = d.toObject(Order.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                OrderArrayList.add(c);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifuDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            OrdersAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we do not get any data or any error we are displaying
                // a toast message that we do not get any data
                Toast.makeText(getContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
