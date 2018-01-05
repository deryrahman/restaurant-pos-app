package com.blibli.future.pos.restaurant.common.model.custom;

import com.blibli.future.pos.restaurant.common.model.BaseResource;
import com.blibli.future.pos.restaurant.common.model.Receipt;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class Ledger extends BaseResource {
    private Timestamp startTime;
    private Timestamp endTime;
    private BigDecimal total;
    private Integer restaurantId;
    private List<Receipt> receipts;

    public Ledger() {
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    @Override
    public String toString() {
        return "Ledger{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", total=" + total +
                ", restaurantId=" + restaurantId +
                ", receipts=" + receipts +
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return null;
    }

    @Override
    public Boolean notValidAttribute() {
        return null;
    }

    @Override
    public Map<String, String> requiredAttribute() {
        return null;
    }
}
