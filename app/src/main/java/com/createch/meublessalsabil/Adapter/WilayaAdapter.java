package com.createch.meublessalsabil.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WilayaAdapter extends BaseAdapter {
    Context context;
    String[] countryNames;
    LayoutInflater inflter;

    public WilayaAdapter(Context applicationContext, String[] countryNames) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryNames.length;
    }

    @Override
    public String getItem(int i) {
        return countryNames[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView names = view.findViewById(android.R.id.text1);
        names.setText(countryNames[i]);
        return view;
    }
}