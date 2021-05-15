package com.createch.meublessalsabil.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.Adapter.WilayaAdapter;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Adresse;
import com.createch.meublessalsabil.models.Order;
import com.createch.meublessalsabil.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;


public class ConfirmShopingList extends Fragment {

    public static String which_state = "fill";
    View thnks, linearLayout, adr_infos;
    CheckBox save_adr;
    public static boolean change_infos = true;
    Adresse adresse;
    Button confirm, go_shopping;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    Spinner wilaya_spinner;
    String user_wilaya;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ImageView imagear, imagefr;
    String ref;
    Double tot;
    Adresse origAdr = null;
    MaterialCardView use_saved, change;

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
        imagefr = view.findViewById(R.id.imagefr);
        imagear = view.findViewById(R.id.imagear);
        go_shopping = view.findViewById(R.id.go_shopping);
        use_saved = view.findViewById(R.id.saved_infos_card);
        change = view.findViewById(R.id.change_infos_card);
        save_adr = view.findViewById(R.id.save_adr);
        wilaya_spinner = view.findViewById(R.id.wilaya_spinner);
        Bundle b = getArguments();
        if (b != null) {
            ref = b.getString("ref");
            tot = b.getDouble("tot");
        } else
            getActivity().onBackPressed();
        WilayaAdapter adapter = new WilayaAdapter(getContext(), getResources().getStringArray(R.array.wilayas));
        wilaya_spinner.setAdapter(adapter);
        wilaya_spinner.setSelection(15);


        wilaya_spinner.setPrompt(" Wilayas");
        wilaya_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_wilaya = adapter.getItem(position);
                Log.d("hbhb", "onItemSelected: " + adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user_wilaya = adapter.getItem(15);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagear.setImageResource(R.drawable.check_off);
                imagefr.setImageResource(R.drawable.check_on);
                change_infos = true;

            }
        });
        use_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagear.setImageResource(R.drawable.check_on);
                imagefr.setImageResource(R.drawable.check_off);

                change_infos = false;
            }
        });
        // if he has adresse ...
        FirebaseFirestore.getInstance().collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                origAdr = documentSnapshot.toObject(User.class).getAdresse();
                if (origAdr != null)
                    choseNeworOldAdresse();

            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (which_state.equals("fill")) {
                    if (FieldsFilled(view)) {
                        if (save_adr.isChecked()) {
                            pushAdresse();
                        }
                        pushOrder(adresse);
                    }
                    pushPhone(view);
                } else if (which_state.equals("choose")) {
                    if (change_infos)
                        FillInfos();
                    else
                        pushOrder(origAdr);

                }
            }
        });
        go_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void pushPhone(View v) {
        FirebaseFirestore.getInstance().collection("Users").document(uid)
                .update("phone", ((EditText) v.findViewById(R.id.phone)).getText().toString().trim());
    }

    private void pushAdresse() {
        FirebaseFirestore.getInstance().collection("Users").document(uid)
                .update("adresse", adresse);
    }

    private boolean FieldsFilled(View v) {
        boolean emptyField = true;
        EditText phone = v.findViewById(R.id.phone);
        EditText adr1 = v.findViewById(R.id.adr1);
        EditText adr2 = v.findViewById(R.id.adr2);
        EditText zipcode = v.findViewById(R.id.zipcode);
        EditText baladia = v.findViewById(R.id.baladia);
        if (TextUtils.isEmpty(adr1.getText().toString().trim())) {
            adr1.requestFocus();
            adr1.setError("this field must not be empty");
            return false;
        }

        if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            phone.requestFocus();
            phone.setError("this field must not be empty");
            return false;
        }
        if (TextUtils.isEmpty(zipcode.getText().toString().trim())) {
            zipcode.requestFocus();
            zipcode.setError("this field must not be empty");
            return false;
        }

        if (TextUtils.isEmpty(baladia.getText().toString().trim())) {
            baladia.requestFocus();
            baladia.setError("this field must not be empty");
            return false;
        }


        adresse = new Adresse(adr1.getText().toString().trim(), adr2.getText().toString().trim(),
                Integer.parseInt(zipcode.getText().toString().trim()),
                baladia.getText().toString().trim(), user_wilaya);
        return emptyField;
    }

    private void ThanksForBuying() {
        adr_infos.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        thnks.setVisibility(View.VISIBLE);
        which_state = "thanks";

    }

    private void FillInfos() {
        linearLayout.setVisibility(View.GONE);
        thnks.setVisibility(View.GONE);
        confirm.setVisibility(View.VISIBLE);
        adr_infos.setVisibility(View.VISIBLE);
        which_state = "fill";

    }

    private void choseNeworOldAdresse() {
        thnks.setVisibility(View.GONE);
        adr_infos.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        which_state = "choose";
    }

    void pushOrder(Adresse ddr) {
        // push order .....
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Timestamp> date = new HashMap<>();
        date.put(Order.WAITING, Timestamp.now());
        map.put("date", date);
        map.put("userId", uid);
        map.put("adresse", ddr);
        map.put("state", Order.WAITING);
        map.put("totalPrice", tot);
        DatabaseReference pushed = FirebaseDatabase.getInstance().getReference().child("PushedOrders").push();
        DatabaseReference refer = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);
        refer.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
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
                                refer.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        ThanksForBuying();
                                    }
                                });
                            }
                        });

                    }
                });
            }
        });

    }
}