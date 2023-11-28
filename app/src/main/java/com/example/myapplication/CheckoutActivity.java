package com.example.myapplication;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private TextView itemNameTextView, itemDescTextView, itemPriceTextView, totalTextView;
    private EditText quantityEditText, addressEditText;
    private Button submitButton;
    private double total = 0;
    int selectedItemPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        itemNameTextView = findViewById(R.id.checkout_itemName);
        itemDescTextView = findViewById(R.id.checkout_itemDesc);
        itemPriceTextView = findViewById(R.id.checkout_itemPrice);
        totalTextView = findViewById(R.id.checkout_total);

        quantityEditText = findViewById(R.id.checkoutQuantity);
        addressEditText = findViewById(R.id.editTextTextMultiLine);

        submitButton = findViewById(R.id.checkout_submit);

        Intent intent = getIntent();
        String selectedItemName = intent.getStringExtra("selectedItemName");
        String selectedItemPriceStr = intent.getStringExtra("selectedItemPrice");
        String selectedItemID = intent.getStringExtra("selectedItemID");
        String selectedItemDesc = intent.getStringExtra("selectedItemDesc");

        String selectedItemPriceCleaned = selectedItemPriceStr.replaceAll("[^\\d]", "");

        selectedItemPrice = parseInt(selectedItemPriceCleaned);

        String formattedPrice = "Rp." + NumberFormat.getInstance().format(selectedItemPrice);
        itemPriceTextView.setText(formattedPrice);

        itemNameTextView.setText(selectedItemName);
        itemDescTextView.setText(selectedItemDesc);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> userDetails = sessionManager.getUserDetails();
                String userId = userDetails.get(SessionManager.ID_USER);
                String item_id = selectedItemID;
                String item_quantity = quantityEditText.getText().toString();
                String order_address = addressEditText.getText().toString();

                sendOrderData(userId, item_id, item_quantity, order_address);
            }
        });
    }


    private void sendOrderData(String user_id, String item_id, String item_quantity, String order_address) {
        String url = "http://10.0.2.2:50/PrimeTools/addOrder.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log or print the response to see what it contains
                        Log.d("ResponseDOngs", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), ListOrderActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CheckoutActivity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CheckoutActivity.this, "Volley error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("item_id", item_id);
                params.put("item_quantity", item_quantity);
                total = parseInt(item_quantity) * selectedItemPrice;
                params.put("order_price", String.valueOf(total));
                params.put("order_address", order_address);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
