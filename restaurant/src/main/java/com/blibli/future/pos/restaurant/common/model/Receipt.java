package com.blibli.future.pos.restaurant.common.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Receipt extends BaseResource {
    private Integer id;
    private Timestamp timestampCreated;
    private Integer restaurantId;
    private Integer userId;
    private Integer memberId;
    private BigDecimal totalPrice;
    private String note;

    public Receipt() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
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

    @Override
    public Boolean isEmpty() {
        return id == null;
    }

    @Override
    public Boolean notValidAttribute() {
        return (restaurantId == null || userId == null || totalPrice == null);
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(restaurantId == null) required.put("restaurantId", "Integer");
        if(userId == null) required.put("userId", "Integer");
        if(totalPrice == null) required.put("totalPrice", "Decimal");
        return required;
    }
}
