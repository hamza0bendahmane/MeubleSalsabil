package com.createch.meublessalsabil.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.createch.meublessalsabil.Activity.Application;
import com.createch.meublessalsabil.Adapter.ShopListAdapter;
import com.createch.meublessalsabil.R;
import com.createch.meublessalsabil.models.Order;
import com.createch.meublessalsabil.models.Soldable;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetails extends Fragment {

    TextView delivered, accepted, onway, price, order_number;
    ImageView delivered_image, accepted_image, onway_image;
    DatabaseReference refer;
    MaterialButton phone_moderator;
    CardView user_infos_card, infos;
    RecyclerView soldables;
    ShopListAdapter adapter;
    LinearLayout reconsidering, delivered_layout;
    Button cancel_name, confirm_name;
    String ref;
    Order thisOrder;
    String itemsRef, state;

    public OrderDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        soldables = view.findViewById(R.id.soldables_order_details);
        reconsidering = view.findViewById(R.id.reconsidering);
        delivered_layout = view.findViewById(R.id.delivered_layout);
        cancel_name = view.findViewById(R.id.cancel_name);
        confirm_name = view.findViewById(R.id.go_shopping);
        infos = view.findViewById(R.id.infos);
        user_infos_card = view.findViewById(R.id.user_infos_card);
        onway = view.findViewById(R.id.onway_date);
        delivered = view.findViewById(R.id.delivered_date);
        accepted = view.findViewById(R.id.accepted_date);
        onway_image = view.findViewById(R.id.onway_image);
        delivered_image = view.findViewById(R.id.delivered_image);
        accepted_image = view.findViewById(R.id.accepted_image);
        phone_moderator = view.findViewById(R.id.phone_moderator);
        order_number = view.findViewById(R.id.order_number);
        price = view.findViewById(R.id.price);

        if (b != null) {
            ref = b.getString("ref");
            itemsRef = b.getString("items");
            state = b.getString("state");
            if (state.equals(Order.WAITING)) {
                setAccepted();
            } else if (state.equals(Order.ONWAY)) {
                OrderOnWayUI();
            } else if (state.equals(Order.DELIVERED)) {
                OrderDeliveredUi();
            }
            refer = FirebaseDatabase.getInstance().getReferenceFromUrl(itemsRef);
            FirebaseFirestore.getInstance().collection("Orders")
                    .document(ref).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    thisOrder = documentSnapshot.toObject(Order.class);
                    accepted.setText(((Timestamp) thisOrder.getDate().get(Order.WAITING)).toDate().toString());
                    if (state.equals(Order.WAITING)) {
                        setAccepted();
                        fillTheGenralInfos();
                    } else if (state.equals(Order.ONWAY)) {
                        onway.setText(((Timestamp) thisOrder.getDate().get(Order.ONWAY)).toDate().toString());
                        OrderOnWayUI();
                        fillTheGenralInfos();
                        fillInfos();
                    } else if (state.equals(Order.DELIVERED)) {
                        onway.setText(((Timestamp) thisOrder.getDate().get(Order.ONWAY)).toDate().toString());
                        delivered.setText(((Timestamp) thisOrder.getDate().get(Order.DELIVERED)).toDate().toString());
                        OrderDeliveredUi();
                    }
                }
            });

        } else
            getActivity().onBackPressed();

        fetchShopList();
        //  getStatus of ordr then update ...
// delivered , on way , accepted ,waiting ...
        // update image and date
        // and updateUI onway or delivered ...


    }

    private void fillTheGenralInfos() {
        price.setText(thisOrder.getTotalPrice() + getString(R.string.currency));
        order_number.setText("#" + ref.substring(0, 7));
        phone_moderator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + Application.ADMINE_PHONE));
                startActivity(intent);
            }
        });

    }

    void fetchShopList() {

        FirebaseRecyclerOptions<Soldable> options;
        options = new FirebaseRecyclerOptions.Builder<Soldable>()
                .setQuery(refer, Soldable.class)
                .build();
        adapter = new ShopListAdapter(options, getContext(), false);
        soldables.setLayoutManager(new LinearLayoutManager(getContext()));
        soldables.setHasFixedSize(true);
        soldables.setAdapter(adapter);

    }

    void setAccepted() {
        accepted_image.setImageResource(R.drawable.confirm_icon);
    }

    void setOnWay() {
        onway_image.setImageResource(R.drawable.confirm_icon);

    }

    void setDelivered() {
        delivered_image.setImageResource(R.drawable.confirm_icon);

    }


    void OrderDeliveredUi() {
        setAccepted();
        setOnWay();
        setDelivered();
        cancel_name.setVisibility(View.GONE);
        confirm_name.setVisibility(View.GONE);
        reconsidering.setVisibility(View.GONE);
        infos.setVisibility(View.GONE);
        soldables.setVisibility(View.GONE);
        adapter.stopListening();
        adapter = null;
        user_infos_card.setVisibility(View.GONE);
        delivered_layout.setVisibility(View.VISIBLE);

    }

    void OrderOnWayUI() {
        setAccepted();
        setOnWay();
        cancel_name.setVisibility(View.GONE);
        confirm_name.setVisibility(View.GONE);
        reconsidering.setVisibility(View.GONE);
        infos.setVisibility(View.VISIBLE);
    }

    private void fillInfos() {
        View view = getView();
        TextView idorder, text_date, prix_delivery, pritotal, prix_orders;
        idorder = view.findViewById(R.id.idorder);
        text_date = view.findViewById(R.id.text_date);
        prix_orders = view.findViewById(R.id.prix_orders);
        prix_delivery = view.findViewById(R.id.prix_delivery);
        pritotal = view.findViewById(R.id.pritotal);

        idorder.setText("#" + ref.substring(0, 7));
        text_date.setText(((Timestamp) thisOrder.getDate().get(Order.WAITING)).toDate().toString());
        prix_orders.setText((thisOrder.getTotalPrice() - thisOrder.getDelivery()) + getString(R.string.currency));
        prix_delivery.setText(thisOrder.getDelivery() + getString(R.string.currency));
        pritotal.setText(thisOrder.getTotalPrice() + getString(R.string.currency));
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