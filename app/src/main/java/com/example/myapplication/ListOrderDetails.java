package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListOrderDetails extends AppCompatActivity {

    private TextView orderDetailsTitle, itemName, orderPrice, orderAddress, isFinished, userEmail, userPhone;
    Button orderFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order_details);

        orderDetailsTitle = findViewById(R.id.orderDetailsTitle);
        itemName = findViewById(R.id.itemName);
        orderPrice = findViewById(R.id.orderPrice);
        orderAddress = findViewById(R.id.orderAddress);
        isFinished = findViewById(R.id.isFinished);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.userPhone);
        orderFinish = findViewById(R.id.orderFinish);

        String orderId = getIntent().getStringExtra("order_id");
        if (orderId != null) {
            fetchOrderDetails(orderId);
        } else {
            Toast.makeText(this, "Order ID not provided", Toast.LENGTH_SHORT).show();
        }

        orderFinish.setOnClickListener(v -> {
            if (orderId != null) {
                updateOrderStatus(orderId);
            } else {
                Toast.makeText(this, "Order ID not provided", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderStatus(String orderId) {
        String url = "http://10.0.2.2:50/PrimeTools/finishOrder.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "JSON parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Volley error", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", orderId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void fetchOrderDetails(String orderId) {
        String url = "http://10.0.2.2:50/PrimeTools/showOrderByID.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                JSONObject orderDetails = jsonResponse.getJSONObject("data");
                                displayOrderDetails(orderDetails);
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to fetch order details", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Volley error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", orderId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void displayOrderDetails(JSONObject orderDetails) throws JSONException {
        orderDetailsTitle.setText("Order Details for Order ID: " + orderDetails.getString("order_id"));

        itemName.setText(orderDetails.getString("item_name"));
        orderPrice.setText(orderDetails.getString("order_price"));
        orderAddress.setText(orderDetails.getString("order_address"));

        int isFinishedValue = orderDetails.getInt("isFinished");
        isFinished.setText(isFinishedValue == 1 ? "Finished" : "Not Finished");

        userEmail.setText(orderDetails.getString("user_email"));
        userPhone.setText(orderDetails.getString("user_phone"));
    }
}