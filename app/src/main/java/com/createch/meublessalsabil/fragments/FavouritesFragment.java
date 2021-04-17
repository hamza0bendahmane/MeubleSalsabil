package com.createch.meublessalsabil.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.createch.meublessalsabil.R;

public class FavouritesFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favourites, container, false);

        /*TODO*/
        //fetch favourites
        //navigate to favourite details
        //delete from favourite
        //navigate to call_us
        //navigate to notification

        return root;
    }
}