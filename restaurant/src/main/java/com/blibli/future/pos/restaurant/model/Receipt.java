package com.blibli.future.pos.restaurant.model;

import java.sql.Timestamp;

public class Receipt {
    private int id;
    private int restaurant_id;
    private int user_id;
    private int member_id;
    private Timestamp timestamp;
    private String total_price;
    private String text;

    public Receipt() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
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
                ", restaurant_id=" + restaurant_id +
                ", user_id=" + user_id +
                ", member_id=" + member_id +
                ", timestamp=" + timestamp +
                ", total_price='" + total_price + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
