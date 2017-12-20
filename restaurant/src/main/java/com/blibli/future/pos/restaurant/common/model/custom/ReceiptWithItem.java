package com.blibli.future.pos.restaurant.common.model.custom;

import com.blibli.future.pos.restaurant.common.model.BaseResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptWithItem extends BaseResource {
    private Integer receiptId;
    private List<ItemOnReceipt> items;

    public ReceiptWithItem() {
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public List<ItemOnReceipt> getItems() {
        return items;
    }

    public void setItems(List<ItemOnReceipt> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ReceiptWithItem{" +
                "receiptId=" + receiptId +
                ", items=" + items +
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return receiptId == null;
    }

    @Override
    public Boolean notValidAttribute() {
        return receiptId == null || items == null;
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(receiptId == null) required.put("receiptId", "Integer");
        if(items == null) required.put("items", "List of ItemOnReceipt");
        return required;
    }
}
