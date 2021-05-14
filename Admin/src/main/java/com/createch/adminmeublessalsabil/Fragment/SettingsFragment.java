package com.createch.adminmeublessalsabil.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.createch.adminmeublessalsabil.Activity.Application;
import com.createch.adminmeublessalsabil.Activity.Login;
import com.createch.adminmeublessalsabil.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class    SettingsFragment extends Fragment {


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ConstraintLayout logout_card, lang_card, infos_card;
    MaterialButton about_app;
    TextView fullName, emailTextView, phoneTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, null);

    }


    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        // ((BottomNavigationView)getActivity().findViewById(R.id.nav_view)).setSelectedItemId(R.id.navigation_settings);
        fullName = root.findViewById(R.id.full_name);
        emailTextView = root.findViewById(R.id.email);
        phoneTextView = root.findViewById(R.id.phone);
        about_app = root.findViewById(R.id.aboutheapp);
        logout_card = root.findViewById(R.id.logout_card);
        lang_card = root.findViewById(R.id.lang_card);
        infos_card = root.findViewById(R.id.infos_card);




        infos_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.nav_host_fragment, new AdminInfosFragment())
                        .commit();
            }
        });
        lang_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(getContext());
                d.setContentView(R.layout.language);
                ImageView fr = d.findViewById(R.id.imagefr);
                ImageView ar = d.findViewById(R.id.imagear);
                if (Application.getCurrentLang(getContext()).equals("ar")) {
                    fr.setImageResource(R.drawable.nselected);
                    ar.setImageResource(R.drawable.selected);

                } else {
                    fr.setImageResource(R.drawable.selected);
                    ar.setImageResource(R.drawable.nselected);
                }
                d.findViewById(R.id.arabic_card).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Application.changeLang("ar", getActivity(), false);
                        refresh();
                        d.dismiss();
                    }
                });
                d.findViewById(R.id.francais_card).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Application.changeLang("fr", getActivity(), false);
                        refresh();
                        d.dismiss();
                    }
                });
                d.show();
            }
        });

        logout_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setMessage("Are u sure want to logout ?");
                dialog.setButton(Dialog.BUTTON_POSITIVE, "logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), Login.class));
                        getActivity().finish();
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
        });
        about_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .add(R.id.nav_host_fragment, new AboutApp()).addToBackStack("app")
                        .commit();
            }
        });
    }

    private void refresh() {
        FragmentManager fragmentManager = getParentFragmentManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragmentManager.beginTransaction().detach(this).commitNow();
            fragmentManager.beginTransaction().attach(this).commitNow();
        } else {
            fragmentManager.beginTransaction().detach(this).attach(this).commit();
        }
    }
}