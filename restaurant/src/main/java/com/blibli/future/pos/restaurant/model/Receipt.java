package com.blibli.future.pos.restaurant.model;

import java.sql.Timestamp;

public class Receipt {
    private int id;
    private Timestamp timestampCreated;
    private int restaurantId;
    private int userId;
    private int memberId;
    private String totalPrice;
    private String text;

    public Receipt() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }


    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", timestampCreated=" + timestampCreated +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                ", memberId=" + memberId +
                ", totalPrice='" + totalPrice + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
