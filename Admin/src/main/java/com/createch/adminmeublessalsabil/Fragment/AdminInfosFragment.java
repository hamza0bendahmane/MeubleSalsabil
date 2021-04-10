package com.createch.adminmeublessalsabil.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.adminmeublessalsabil.R;
import com.google.android.material.card.MaterialCardView;

public class AdminInfosFragment extends Fragment {
    MaterialCardView pass, name, phone, adr, workh;

    public AdminInfosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        pass = view.findViewById(R.id.pass_card);
        name = view.findViewById(R.id.name_card);
        phone = view.findViewById(R.id.phone_card);
        adr = view.findViewById(R.id.adr_card);
        workh = view.findViewById(R.id.workh_card);

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (view.findViewById(R.id.edit_pass_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.password).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_pass_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im1).setRotation(0);

                } else {
                    view.findViewById(R.id.password).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_pass_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im1).setRotation(270);

                }

            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.edit_name_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.full_name).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_name_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im3).setRotation(0);

                } else {
                    view.findViewById(R.id.full_name).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_name_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im3).setRotation(270);

                }


            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.edit_phone_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.phone).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_phone_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im2).setRotation(0);

                } else {
                    view.findViewById(R.id.phone).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_phone_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im2).setRotation(270);

                }

            }
        });
        adr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.edit_adr_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.adr).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_adr_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im4).setRotation(0);

                } else {
                    view.findViewById(R.id.adr).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_adr_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im4).setRotation(270);

                }

            }
        });
        workh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(R.id.edit_workh_layout).getVisibility() == View.VISIBLE) {
                    view.findViewById(R.id.workh).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit_workh_layout).setVisibility(View.GONE);
                    view.findViewById(R.id.im5).setRotation(0);

                } else {
                    view.findViewById(R.id.workh).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.edit_workh_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.im5).setRotation(270);

                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_infos, container, false);
    }

}