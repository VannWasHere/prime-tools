package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Orders.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ORDER_PRICE = "order_price";
    public static final String COLUMN_ORDER_ADDRESS = "order_address";
    public static final String COLUMN_IS_FINISHED = "is_finished";

    private static final String CREATE_ORDERS_TABLE =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ORDER_ID + " TEXT," +
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
        // Implement if needed
    }

    public boolean insertOrder(String orderId, String itemName, String orderPrice, String orderAddress, int isFinished) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_ID, orderId);
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_ORDER_PRICE, orderPrice);
        values.put(COLUMN_ORDER_ADDRESS, orderAddress);
        values.put(COLUMN_IS_FINISHED, isFinished);

        long newRowId = db.insert(TABLE_ORDERS, null, values);

        db.close();

        return newRowId != -1;
    }
}