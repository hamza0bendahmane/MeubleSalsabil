package com.createch.adminmeublessalsabil.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.adminmeublessalsabil.Activity.Login;
import com.createch.adminmeublessalsabil.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    MaterialCardView lang, infos, logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        lang = view.findViewById(R.id.language_card);
        infos = view.findViewById(R.id.infos_card);
        logout = view.findViewById(R.id.logout_card);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context;
                AlertDialog dd = new AlertDialog.Builder(getContext()).create();
                dd.setTitle("log out");
                dd.setButton(Dialog.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dd.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), Login.class));
                    }
                });
                dd.setMessage("Are u sure wanna log out");
                dd.show();

            }
        });
        infos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ff = new AdminInfosFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, ff)
                        .addToBackStack(ff.getTag())
                        .commit();
            }
        });
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ff = new LangFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, ff)
                        .addToBackStack(ff.getTag())
                        .commit();
            }
        });
    }
}