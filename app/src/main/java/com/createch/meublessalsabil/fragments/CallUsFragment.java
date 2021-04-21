package com.createch.meublessalsabil.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.R;

import java.util.Locale;


public class CallUsFragment extends Fragment {

    String email_frombdd, phone_frombdd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_call_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView maps = view.findViewById(R.id.maps_int);
        TextView maps0 = view.findViewById(R.id.maps_int0);
        Button maps1 = view.findViewById(R.id.maps_int1);

        ImageView phone = view.findViewById(R.id.phone_int);
        TextView phone1 = view.findViewById(R.id.phone_int1);

        ImageView email = view.findViewById(R.id.email_int);
        TextView email1 = view.findViewById(R.id.email_int1);


        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone();

            }
        });
        email1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mail();
            }
        });
        maps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map();
            }
        });
        maps0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map();

            }
        });
//......
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail();

            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone();
            }
        });

    }


    void mail() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email_frombdd)); // only email apps should handle this
        getActivity().startActivity(intent);
    }

    void phone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone_frombdd)); // only email apps should handle this
        getActivity().startActivity(intent);

    }


    void map() {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 36.6384246, 2.7675887);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        getContext().startActivity(intent);

    }

}