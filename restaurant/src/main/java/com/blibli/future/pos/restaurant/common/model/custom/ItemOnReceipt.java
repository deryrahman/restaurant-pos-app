package com.blibli.future.pos.restaurant.common.model.custom;

import com.blibli.future.pos.restaurant.common.model.BaseResource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ItemOnReceipt extends BaseResource {
    private Integer itemId;
    private String itemName;
    private Integer count;
    private BigDecimal subTotal;

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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
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
        return itemId == null || notValidAttribute();
    }

    @Override
    public Boolean notValidAttribute() {
        return (itemId == null || count == null || count < 0);
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(itemId == null) required.put("itemId", "Integer");
        if(count == null) required.put("count", "Integer");
        return required;
    }
}
