package com.createch.adminmeublessalsabil.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.createch.adminmeublessalsabil.Adapter.ShopListAdapter;
import com.createch.adminmeublessalsabil.Model.Order;
import com.createch.adminmeublessalsabil.Model.Soldable;
import com.createch.adminmeublessalsabil.Model.User;
import com.createch.adminmeublessalsabil.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetails extends Fragment {

    String refer ,state ;
    MaterialButton cancel , submit;
    DocumentReference orderReference;
    TextView name ,phone ,email , infos ,price , order_id , date ,time ;
    RecyclerView soldables ;
    ShopListAdapter adapter ;
    CircleImageView user_image ;
    DatabaseReference reference;
    DocumentReference docRef;
    User who_makes_order;
    double totaPrice,deliveryprice;

    HashMap<String, Timestamp> mapDate ;
    ImageView about_user ;
    Order thisOrder;

    public OrderDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext() fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View thsv, @Nullable Bundle savedInstanceState) {
        Bundle ii = getArguments();
        refer = ii.getString("ref");
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(ii.getString("items"));
        state = ii.getString("state");
        orderReference = FirebaseFirestore.getInstance().collection("Orders").document(refer);
        soldables = thsv.findViewById(R.id.soldables);
        setState(thsv);
        fetchShopList();
        filltheFields(thsv);



    }
    private void setState(View thsv) {
        cancel = thsv.findViewById(R.id.cancel_name);
        submit = thsv.findViewById(R.id.submit_name);
        if (state.equals(Order.WAITING)) {
            submit.setText(R.string.onway);
            submit.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        } else if (state.equals(Order.ONWAY)) {
            submit.setText(R.string.delivered);
            submit.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);
        } else if (state.equals(Order.DELIVERED)) {
            submit.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitOrder(thsv);
                }
            });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            cancelOrder(thsv);
            }
        });


    }

    private void filltheFields(View thsv) {

        price = thsv.findViewById(R.id.price);
        time = thsv.findViewById(R.id.time);
        date = thsv.findViewById(R.id.date);
        order_id = thsv.findViewById(R.id.order_number);
        infos = thsv.findViewById(R.id.adresses);
        phone = thsv.findViewById(R.id.materials);
        name = thsv.findViewById(R.id.name_product);
        email = thsv.findViewById(R.id.text_email);
        user_image = thsv.findViewById(R.id.image_product);
        about_user = thsv.findViewById(R.id.im3);
        orderReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                thisOrder = documentSnapshot.toObject(Order.class);
                order_id.setText(documentSnapshot.getId());
                mapDate = thisOrder.getDate();
                date.setText(thisOrder.getDate().get(Order.WAITING).toDate().toString());
                fillTheGenralInfos();
                FirebaseFirestore.getInstance().collection("Users").document(documentSnapshot.get("userId").toString())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        who_makes_order = documentSnapshot.toObject(User.class);

                        infos.setText(new StringBuilder().append(who_makes_order.getAdresse().getWilaya()).
                                append(" ,").append(who_makes_order.getAdresse().getFirstAdr()));
                        name.setText(who_makes_order.getFname() + " " +who_makes_order.getLname());
                        email.setText(who_makes_order.getEmail());
                        phone.setText(who_makes_order.getPhone());
                        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .override(250, 200) ;
                        Glide.with(getContext()).load(who_makes_order.getPhoto()).apply(requestOptions).into(user_image);
                    }
                });


            }
        });

        about_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tools = thsv.findViewById(R.id.edit_user);
                if (tools.getVisibility() == View.GONE)
                    tools.setVisibility(View.VISIBLE);
                else
                    tools.setVisibility(View.GONE);
            }
        });

        thsv.findViewById(R.id.mail_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + who_makes_order.getEmail())); // only email apps should handle getContext()
                startActivity( intent);

            }
        });
        thsv.findViewById(R.id.phone_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call user
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + who_makes_order.getPhone())); // only email apps should handle getContext()
                startActivity( intent);
            }
        });
        thsv.findViewById(R.id.block_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // block user
                String uid = who_makes_order.getUid();
                new AlertDialog.Builder(getContext()).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // block user
                        FirebaseFirestore.getInstance().collection("Users").document(uid).update("blocked", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference().child("BlockedUsers").child(uid).
                                        setValue(true).
                                        addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "blocked", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        });

                    }
                })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setTitle("تأكيد").
                        setMessage("هل انت متأكد من حظر المستخدم").show();






            }
        });





    }
    private void fillTheGenralInfos() {
        price.setText(thisOrder.getTotalPrice() + getString(R.string.currency));
        ((TextView)getView().findViewById(R.id.order_number)).setText("#" + refer.substring(0, 7));
        getView().findViewById(R.id.phone_moderator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + who_makes_order.getPhone()));
                startActivity(intent);
            }
        });

    }
    public void cancelOrder(View vv){
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setMessage("Are u sure want delete your order ?");
        dialog.setButton(Dialog.BUTTON_POSITIVE, "Yes , Iam sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        orderReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(),R.string.successful,Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();

                            }
                        });

                    }
                });
            }
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }
    public void submitOrder(View vv){
        if (!state.equals(Order.ONWAY)) {
            Context context = getContext();
            Dialog d = new Dialog(getContext());
            d.setContentView(R.layout.final_price);
            EditText delivery = d.findViewById(R.id.prix_delivery);
            TextView total = d.findViewById(R.id.pritotal);
            TextView price = d.findViewById(R.id.prix_orders);
            price.setText(String.valueOf(thisOrder.getTotalPrice()));
            total.setText(String.valueOf(thisOrder.getTotalPrice()));
            delivery.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    double ii = 0 ;
                    if (s.toString().isEmpty())
                        ii =  thisOrder.getTotalPrice();
                    else
                        ii = Double.parseDouble(s.toString()) + thisOrder.getTotalPrice();
                    total.setText(String.valueOf(ii));
                }
            });
            d.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deliveryprice =Double.parseDouble(delivery.getText().toString());
                    totaPrice =thisOrder.getTotalPrice() + deliveryprice;

                    ProgressDialog dialog = new ProgressDialog(context);
                    dialog.setTitle("Uploading ...");
                    dialog.show();
                    mapDate.put(Order.ONWAY, Timestamp.now());
                    orderReference.update("delivery",deliveryprice ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            orderReference.update("totalPrice",totaPrice).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    orderReference.update("date", mapDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            orderReference.update("state", Order.ONWAY).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    dialog.dismiss();
                                                    Snackbar.make(vv, "تم قبول الطلب", Snackbar.LENGTH_SHORT).show();
                                                    d.dismiss();

                                                }
                                            });
                                        }
                                    });

                                }
                            });
                        }
                    });
                }
            });
            d.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            Window window = d.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            d.show();


        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            dialog.setMessage("Are u sure want mark this order as delivered?");
            dialog.setButton(Dialog.BUTTON_POSITIVE, "Yes , Iam sure", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mapDate.put(Order.DELIVERED, Timestamp.now());
                    orderReference.update("date", mapDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            orderReference.update("state", Order.DELIVERED).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Snackbar.make(vv, "تم توصيل الطلب", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
            dialog.setButton(Dialog.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }
    void fetchShopList() {

        FirebaseRecyclerOptions<Soldable> options;
        options = new FirebaseRecyclerOptions.Builder<Soldable>()
                .setQuery(reference, Soldable.class)
                .build();
        adapter = new ShopListAdapter(options, getContext(), false);
        soldables.setLayoutManager(new LinearLayoutManager(getContext()));
        soldables.setAdapter(adapter);

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