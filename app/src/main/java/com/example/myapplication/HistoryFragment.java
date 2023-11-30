package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryFragment extends Fragment {
    private ListView historyListView;
    private HistoryListAdapter historyListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        historyListView = v.findViewById(R.id.historyList);

        SessionManager sessionManager = new SessionManager(requireContext());
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        String userId = userDetails.get(SessionManager.ID_USER);

        ArrayList<Order> orderList = fetchOrdersForUser(userId);

        historyListAdapter = new HistoryListAdapter(requireContext(), orderList);
        historyListView.setAdapter(historyListAdapter);
        return v;
    }

    private ArrayList<Order> fetchOrdersForUser(String userId) {
        DBHandler dbHandler = new DBHandler(requireContext());
        return dbHandler.getOrdersForUser(userId);
    }

    public class HistoryListAdapter extends ArrayAdapter<Order> {

        public HistoryListAdapter(Context context, ArrayList<Order> orders) {
            super(context, 0, orders);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.history_list, parent, false);
            }

            Order currentOrder = getItem(position);

            TextView orderListName = listItemView.findViewById(R.id.orderListName);
            TextView orderListPrice = listItemView.findViewById(R.id.orderListPrice);
            TextView orderListAddress = listItemView.findViewById(R.id.orderListAddress);
            TextView orderListStatus = listItemView.findViewById(R.id.orderListStatus);

            orderListName.setText(currentOrder.getItemName());
            orderListPrice.setText(currentOrder.getOrderPrice());
            orderListAddress.setText(currentOrder.getOrderAddress());
            orderListStatus.setText(currentOrder.getIsFinished() == 1 ? "Success" : "Pending");

            return listItemView;
        }
    }
}