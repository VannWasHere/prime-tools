package com.example.myapplication;

public class Order {
    private String orderId;
    private String itemName;
    private String orderPrice;
    private String orderAddress;
    private int isFinished;

    public Order(String orderId, String itemName, String orderPrice, String orderAddress, int isFinished) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.orderAddress = orderAddress;
        this.isFinished = isFinished;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public int getIsFinished() {
        return isFinished;
    }

    // Setters
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }
}
