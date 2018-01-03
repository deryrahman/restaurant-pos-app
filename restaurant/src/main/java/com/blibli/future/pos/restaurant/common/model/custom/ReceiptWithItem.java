package com.blibli.future.pos.restaurant.common.model.custom;

import com.blibli.future.pos.restaurant.common.model.BaseResource;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptWithItem extends BaseResource {
    private Integer receiptId;
    private Integer memberId;
    private String note;
    private BigDecimal tax;
    private Timestamp timestamp;
    private List<ItemOnReceipt> items;

    public ReceiptWithItem() {
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public List<ItemOnReceipt> getItems() {
        return items;
    }

    public void setItems(List<ItemOnReceipt> items) {
        this.items = items;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ReceiptWithItem{" +
                "receiptId=" + receiptId +
                ", note='" + note + '\'' +
                ", tax=" + tax +
                ", items=" + items +
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return receiptId == null || notValidAttribute();
    }

    @Override
    public Boolean notValidAttribute() {
        return items == null || items.isEmpty();
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(items == null || items.isEmpty()) required.put("items", "List of ItemOnReceipt");
        return required;
    }
}
