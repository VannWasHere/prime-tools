package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Orders.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ORDER_PRICE = "order_price";
    public static final String COLUMN_ORDER_ADDRESS = "order_address";
    public static final String COLUMN_IS_FINISHED = "is_finished";

    private static final String CREATE_ORDERS_TABLE =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ORDER_ID + " TEXT," +
                    COLUMN_USER_ID + " INTEGER," +
                    COLUMN_ITEM_NAME + " TEXT," +
                    COLUMN_ORDER_PRICE + " TEXT," +
                    COLUMN_ORDER_ADDRESS + " TEXT," +
                    COLUMN_IS_FINISHED + " INTEGER" +
                    ")";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + TABLE_ORDERS + " ADD COLUMN " + COLUMN_USER_ID + " INTEGER");
    }

    public boolean insertOrder(String orderId, String itemName, String userID, String orderPrice, String orderAddress, int isFinished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderId);
        values.put(COLUMN_USER_ID, userID);
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_ORDER_PRICE, orderPrice);
        values.put(COLUMN_ORDER_ADDRESS, orderAddress);
        values.put(COLUMN_IS_FINISHED, isFinished);

        long newRowId = db.insert(TABLE_ORDERS, null, values);

        db.close();

        return newRowId != -1;
    }

    public ArrayList<Order> getOrdersForUser(String userId) {
        ArrayList<Order> orderList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ORDER_ID, COLUMN_ITEM_NAME, COLUMN_ORDER_PRICE, COLUMN_ORDER_ADDRESS, COLUMN_IS_FINISHED};
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {userId};

        Cursor cursor = db.query(TABLE_ORDERS, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String orderId = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_ID));
                @SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME));
                @SuppressLint("Range") String orderPrice = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_PRICE));
                @SuppressLint("Range") String orderAddress = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_ADDRESS));
                @SuppressLint("Range") int isFinished = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FINISHED));

                Order order = new Order(orderId, itemName, orderPrice, orderAddress, isFinished);
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return orderList;
    }

    public void deleteOrder(String orderId) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = COLUMN_ORDER_ID + " = ?";
        String[] selectionArgs = {orderId};
        db.delete(TABLE_ORDERS, selection, selectionArgs);
        db.close();
    }
}