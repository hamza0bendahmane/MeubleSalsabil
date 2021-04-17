package com.createch.adminmeublessalsabil.Fragment;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.createch.adminmeublessalsabil.R;
import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

public class LangFragment extends Fragment {
    MaterialCardView ar, fr;

    public LangFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ar = view.findViewById(R.id.arabic_card);
        fr = view.findViewById(R.id.francais_card);

        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(getLang() == "ar")) {
                    changeLang("ar");
                }

            }
        });
        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((getLang() == "ar")) {
                    changeLang("fr");

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lang, container, false);
    }

    private void changeLang(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics display = res.getDisplayMetrics();
        Configuration configuration = res.getConfiguration();
        configuration.locale = myLocale;
        res.updateConfiguration(configuration, display);
        Toast.makeText(getContext(), getResources().getString(R.string.language_updated), Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateIcons();
    }

    private void updateIcons() {
        if (getLang().equals("ar")) {
            getView().findViewById(R.id.imagear).setBackgroundResource(R.drawable.selected);
            getView().findViewById(R.id.imagefr).setBackgroundResource(R.drawable.nselected);

        } else {
            getView().findViewById(R.id.imagear).setBackgroundResource(R.drawable.nselected);
            getView().findViewById(R.id.imagefr).setBackgroundResource(R.drawable.selected);
        }
    }

    private String getLang() {
        String language = getResources().getConfiguration().locale.getLanguage();
        Log.d("hbhb", language);

        return language;
    }
}