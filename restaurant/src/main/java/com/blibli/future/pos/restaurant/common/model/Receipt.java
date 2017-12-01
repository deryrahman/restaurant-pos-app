package com.blibli.future.pos.restaurant.common.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Receipt {
    private int id;
    private Timestamp timestampCreated;
    private int restaurantId;
    private int userId;
    private int memberId;
    private BigDecimal totalPrice;
    private String note;
    private String href;
    private String href2;

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


    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void autoSetHref(){
        this.href = "/receipts/" + id;
        this.href2 = "/restaurants/" + restaurantId + this.href;
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
                ", note='" + note + '\'' +
                '}';
    }
}
