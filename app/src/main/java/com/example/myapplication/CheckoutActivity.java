package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();
        String selectedItemName = intent.getStringExtra("selectedItemName");
        String selectedItemPrice = intent.getStringExtra("selectedItemPrice");
        String selectedItemID = intent.getStringExtra("selectedItemID");
        String selectedItemDesc = intent.getStringExtra("selectedItemDesc");
    }
}