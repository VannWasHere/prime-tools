package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    TextView userHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.isLogging();

        HashMap<String, String> user = sessionManager.getUserDetails();
        userHello = findViewById(R.id.userHello);
        userHello.setText("Hello, " + user.get(sessionManager.USERNAME));


    }
}