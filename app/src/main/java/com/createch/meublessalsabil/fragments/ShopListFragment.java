package com.createch.meublessalsabil.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.R;

public class ShopListFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_shoplist, container, false);

        /*TODO*/
        //fetch shoplist items
        //navigate to shoplist item details
        //delete from shoplist
        //navigate to orders
        //navigate to notification

        return root;
    }
}