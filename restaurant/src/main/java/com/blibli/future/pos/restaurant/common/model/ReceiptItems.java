package com.blibli.future.pos.restaurant.common.model;

import java.util.List;


public class ReceiptItems {
    private Receipt receipt;
    private List<Item> items;

    public ReceiptItems() {
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
