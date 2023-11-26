package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    ArrayList<HashMap<String, String>> listRandTools;
    ListView listView;
    ListAdapter adapter;
    String get_rand_item = "http://10.0.2.2:50/PrimeTools/showRandItem.php";
    private static final String RESPONSE_DATA = "items";
    String item_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        listRandTools = new ArrayList<>();
        listView = v.findViewById(R.id.itemList);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_rand_item, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray item = jsonObject.getJSONArray(RESPONSE_DATA);
                    for (int i = 0; i < item.length(); i++) {
                        JSONObject getData = item.getJSONObject(i);
                        String name = getData.getString("item_name");
                        String priceStr = getData.getString("item_price");
                        item_id = getData.getString("item_id");

                        int price = Integer.parseInt(priceStr);

                        String formattedPrice = "Rp." + NumberFormat.getInstance().format(price);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("item_id", item_id);
                        hashMap.put("item_name", name);
                        hashMap.put("item_price", formattedPrice);
                        listRandTools.add(hashMap);
                    }
                    adapter = new SimpleAdapter(getContext(), listRandTools, R.layout.list_item, new String[]{
                            "item_name",
                            "item_price",
                    }, new int[]{
                            R.id.item_name,
                            R.id.item_price
                    });
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItemID = listRandTools.get(position).get("item_id");

                            Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                            intent.putExtra("selectedItemID", selectedItemID);
                            startActivity(intent);
                        }
                    });

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
                Toast.makeText(getContext(), "Silahkan cek koneksi anda", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

        return v;
    }
}
