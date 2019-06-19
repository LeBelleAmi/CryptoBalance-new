package com.lebelle.cryptobalance.model;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lebelle.cryptobalance.R;

import java.util.ArrayList;

/**
 * Created by Omawumi Eyekpimi on 10-Oct-17.
 */

// Custom Adapter for Spinner
public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Context context1;
    private ArrayList<String> data;
    public Resources res;
    LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context, ArrayList<String> objects) {
        super(context, R.layout.action_bar_spinner, objects);

        context1 = context;
        data = objects;

        inflater = (LayoutInflater) context1
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This function called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.action_bar_spinner, parent, false);

        TextView tvCategory = (TextView) row.findViewById(R.id.tvCategory);

        tvCategory.setText(data.get(position).toString());

        return row;
    }
}