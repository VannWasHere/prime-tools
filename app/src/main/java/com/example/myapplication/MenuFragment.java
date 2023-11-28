package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {

    SessionManager sessionManager;
    Button menu_profile, menu_orders, menu_camera, menu_location, menu_web, menu_logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_menu, container, false);

        menu_orders = v.findViewById(R.id.menu_orders);
        menu_orders.setOnClickListener(view -> startActivity(new Intent(getActivity(), ListOrderActivity.class)));

        menu_logout = v.findViewById(R.id.menu_logout);
        sessionManager = new SessionManager(requireContext());

        menu_logout = v.findViewById(R.id.menu_logout);
        sessionManager = new SessionManager(requireContext());

        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager != null) {
                    sessionManager.logout();
                } else {
                    startActivity(new Intent(requireContext(), MainActivity.class));
                }
            }
        });
        return v;
    }
}