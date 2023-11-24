package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private LinearLayout contentLayout;
    private ListView listViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        contentLayout = findViewById(R.id.contentLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.tab_history) {
                replaceFragment(new HistoryFragment());
                return true;
            } else if (itemId == R.id.tab_buy) {
                replaceFragment(new MarketFragment());
                return true;
            } else if (itemId == R.id.tab_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.tab_menu) {
                replaceFragment(new MenuFragment());
                return true;
            }
            return false;
        });
        replaceFragment(new HomeFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
    }
}
