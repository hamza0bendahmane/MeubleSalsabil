package com.createch.adminmeublessalsabil.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.adminmeublessalsabil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class PanelFragment extends Fragment {

        TextView waiting , accepted ,products ,users ;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    public PanelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BottomNavigationView bottom_navigation = getActivity().findViewById(R.id.nav_view);
        if (!bottom_navigation.isShown())
            bottom_navigation.setVisibility(View.VISIBLE);

        waiting = view.findViewById(R.id.waiting_number);
        accepted = view.findViewById(R.id.accepted_number);
        products = view.findViewById(R.id.products_number);
        users = view.findViewById(R.id.users_number);


        db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int countac = 0;
                    int countwait = 0;

                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.get("state").equals("accepted"))
                        countac++;
                        else if (document.get("state").equals("waiting"))
                            countwait++;
                    }
                    waiting.setText((String.valueOf( countwait)));
                    accepted.setText(String.valueOf(countac));

                } else {
                    waiting.setText("******");
                    accepted.setText("******");
                }
            }
        });
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int countac = task.getResult().getDocuments().size();

                    users.setText(String.valueOf(countac));

                } else {
                    users.setText("******");
                }
            }
        });
        db.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int countac = task.getResult().getDocuments().size();
                    products.setText(String.valueOf(countac));

                } else {
                    products.setText("******");
                }
            }
        });
    }
}