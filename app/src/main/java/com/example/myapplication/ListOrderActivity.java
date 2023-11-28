package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOrderActivity extends AppCompatActivity {
    private ListView orderListView;
    private List<Map<String, String>> orderList;
    private SimpleAdapter orderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);

        orderListView = findViewById(R.id.orderList);
        orderList = new ArrayList<>();
        orderListAdapter = new SimpleAdapter(
                this,
                orderList,
                R.layout.order_list,
                new String[]{"itemName", "orderPrice", "orderAddress"},
                new int[]{R.id.orderListName, R.id.orderListPrice, R.id.orderListAddress}
        );
        orderListView.setAdapter(orderListAdapter);
        fetchOrders();
    }

    private void fetchOrders() {
        String url = "http://10.0.2.2:50/PrimeTools/showOrders.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                JSONArray ordersArray = jsonResponse.getJSONArray("orders");

                                orderList.clear();

                                for (int i = 0; i < ordersArray.length(); i++) {
                                    JSONObject orderObject = ordersArray.getJSONObject(i);

                                    String itemName = orderObject.getString("item_name");
                                    int orderPrice = orderObject.getInt("order_price");
                                    String orderAddress = orderObject.getString("order_address");

                                    Map<String, String> orderMap = new HashMap<>();
                                    orderMap.put("itemName", itemName);
                                    orderMap.put("orderPrice", String.valueOf(orderPrice));
                                    orderMap.put("orderAddress", orderAddress);

                                    orderList.add(orderMap);
                                }
                                orderListAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to fetch orders", Toast.LENGTH_SHORT).show();
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
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                String my_id = "2";
                params.put("user_id", my_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
