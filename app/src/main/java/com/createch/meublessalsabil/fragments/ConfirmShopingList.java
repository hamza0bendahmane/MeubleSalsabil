package com.createch.meublessalsabil.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Adresse;
import com.createch.meublessalsabil.models.Order;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class ConfirmShopingList extends Fragment {

    public static String which_state = "fill";
    View thnks, linearLayout, adr_infos;
    CheckBox save_adr;
    Adresse adresse;
    Button confirm, go_shopping;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Order order;

    public ConfirmShopingList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_shoping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        linearLayout = view.findViewById(R.id.linearLayout);
        thnks = view.findViewById(R.id.thnks);
        adr_infos = view.findViewById(R.id.adrresse_infos);
        confirm = view.findViewById(R.id.confirm);
        go_shopping = view.findViewById(R.id.go_shopping);
        save_adr = view.findViewById(R.id.save_adr);
        // if he has adresse ...
        if (false)
            choseNeworOldAdresse();
        if (false)
            ThanksForBuying();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (which_state.equals("fill") && FieldsFilled(view)) {
                    choseNeworOldAdresse();
                    if (save_adr.isChecked()) {
                        pushAdresse();
                    }
                } else if (which_state.equals("choose"))
                    pushOrder();
            }
        });
        go_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void pushAdresse() {
        FirebaseFirestore.getInstance().collection("Users").document(uid)
                .update("adresse", adresse);
    }

    private void pushOrder() {
        FirebaseDatabase.getInstance().getReference().child("Orders").child(uid).push().
                setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ThanksForBuying();
            }
        });
    }

    private boolean FieldsFilled(View v) {
        boolean emptyField = true;

        adresse = new Adresse();
        return emptyField;
    }

    private void ThanksForBuying() {
        adr_infos.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        thnks.setVisibility(View.VISIBLE);
        which_state = "thanks";

    }

    private void choseNeworOldAdresse() {
        adr_infos.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        which_state = "choose";
    }
}