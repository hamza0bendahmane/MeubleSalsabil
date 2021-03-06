package com.createch.meublessalsabil.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.meublessalsabil.Activity.Notifications;
import com.createch.meublessalsabil.Adapter.BestOffersAdapter;
import com.createch.meublessalsabil.Adapter.ProductsAdapter;
import com.createch.meublessalsabil.Adapter.PromotionsAdapter;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Item;
import com.createch.meublessalsabil.models.Promotion;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProductsAdapter prodadapter;
    PromotionsAdapter promadapter;
    BestOffersAdapter boadapter;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());

    public HomeFragment() {


        // Required empty public constructor


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // ((BottomNavigationView)getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_home);
        getNotifications(getView());
        fetchBestOffres(getView());
        fetchPromotions(getView());
        fetchProducts(getView());
        boadapter.startListening();
        prodadapter.startListening();
        promadapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        boadapter.stopListening();
        prodadapter.stopListening();
        promadapter.stopListening();

    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        TextView welcomeText = root.findViewById(R.id.welcome_text);
        checkVeified(root);
        //        Notifications ...

        root.findViewById(R.id.notification_icon).setOnClickListener(new View.OnClickListener() {
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
                        .replace(R.id.nav_host_fragment, new CallUsFragment()).addToBackStack("app")
                        .commit();
            }
        });
        //fetch user data
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firestore Doc", "DocumentSnapshot data: " + document.getData());
                        String firstName = document.getString("fname");
                        welcomeText.setText(getString(R.string.welcome_text, firstName));
                    }
                }
            }
        });

        /*TODO*/
        //search for product
    }

    private void checkVeified(View root) {
        Button opengmail = root.findViewById(R.id.open_gmail);
        TextView desc = root.findViewById(R.id.desc_text);
        if (!user.isEmailVerified()) {

            desc.setText(getString(R.string.verify_mail));
            opengmail.setVisibility(View.VISIBLE);
            opengmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            PackageManager manager = getContext().getPackageManager();
                            Intent i = manager.getLaunchIntentForPackage("com.google.android.gm");
                            i.addCategory(Intent.CATEGORY_LAUNCHER);
                            startActivity(i);
                            FirebaseAuth.getInstance().signOut();
                            getActivity().finish();

                        }
                    });
                }
            });


        } else {
            opengmail.setVisibility(View.GONE);
            desc.setText(getString(R.string.welc_desc));

        }


    }


    void fetchProducts(View root) {
        Query query = FirebaseFirestore.getInstance().collection("Products");
        FirestoreRecyclerOptions<Item> options;
        options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        prodadapter = new ProductsAdapter(options, getContext());
        RecyclerView recyclerView = root.findViewById(R.id.products_recy);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(prodadapter);
    }

    void fetchPromotions(View root) {

        Query query = FirebaseFirestore.getInstance().collection("Promotions");
        FirestoreRecyclerOptions<Promotion> options;
        options = new FirestoreRecyclerOptions.Builder<Promotion>()
                .setQuery(query, Promotion.class)
                .build();
        promadapter = new PromotionsAdapter(options, getContext());
        RecyclerView recyclerView = root.findViewById(R.id.promotions_recy);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(promadapter);

    }

    void fetchBestOffres(View root) {
        Query query = FirebaseFirestore.getInstance().collection("Products");
        FirestoreRecyclerOptions<Item> options;
        options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        boadapter = new BestOffersAdapter(options, getContext());
        RecyclerView recyclerView = root.findViewById(R.id.bestoffres_recy);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(boadapter);

    }

    public void getNotifications(View root) {
        TextView nn = root.findViewById(R.id.number_of_notification);
        fdb.getReference().child("notSeenNotifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Long number = (Long) snapshot.getValue();
                    if (number != 0) {
                        nn.setVisibility(View.VISIBLE);
                        nn.setText(String.valueOf(number));
                    } else
                        nn.setVisibility(View.INVISIBLE);
                }
            }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        nn.setVisibility(View.INVISIBLE);

                    }
                });
    }
}