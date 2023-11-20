package com.example.intervision;

import static android.view.View.GONE;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class SpinnerItem {
    public ArrayList<String> items;
    public View parent;
    public View layout;
    public Activity activity;
    public Spinner dropdown;
    public ArrayAdapter<String> adapter;

    public SpinnerItem(ArrayList<String> items, ViewGroup parent, Activity activity) {
        this.items = items;
        this.parent = parent;
        this.activity = activity;

        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_spinner_item,
                (ViewGroup) parent,
                false);
    }
    public void init(){
        Log.d("Spinnner Item", "Init");
        ((ViewGroup) parent).addView(layout);
        Log.d("Spinnner Item", "Layout");
        dropdown = layout.findViewById(R.id.spinner_user_spinner_item);
        Log.d("Spinnner Item", "DropDown" + "Items: " + items);
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, items);
        Log.d("Spinnner Item", "Adapter");
        dropdown.setAdapter(adapter);
    }
    public void Enable(){
        dropdown.setVisibility(View.VISIBLE);
    }
    public void Disable(){
        dropdown.setVisibility(GONE);
    }
}
