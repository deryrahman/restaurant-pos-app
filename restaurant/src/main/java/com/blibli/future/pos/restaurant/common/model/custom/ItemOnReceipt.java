package com.blibli.future.pos.restaurant.common.model.custom;

import com.blibli.future.pos.restaurant.common.model.BaseResource;

import java.util.HashMap;
import java.util.Map;

public class ItemOnReceipt extends BaseResource {
    private Integer itemId;
    private String itemName;
    private Integer count;
    private Integer subTotal;

    public ItemOnReceipt() {
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public String toString() {
        return "ItemOnReceipt{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", count=" + count +
                ", subTotal=" + subTotal +
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return itemId == null;
    }

    @Override
    public Boolean notValidAttribute() {
        return (itemName == null || count == null || subTotal == null);
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(itemName == null) required.put("itemName", "String");
        if(count == null) required.put("count", "Integer");
        if(subTotal == null) required.put("subTotal", "Decimal");
        return required;
    }
}
