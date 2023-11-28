package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {

    Button menu_profile, menu_orders, menu_camera, menu_location, menu_web;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_menu, container, false);

        menu_orders = v.findViewById(R.id.menu_orders);
        menu_orders.setOnClickListener(view -> startActivity(new Intent(getActivity(), ListOrderActivity.class)));

        return v;
    }
}