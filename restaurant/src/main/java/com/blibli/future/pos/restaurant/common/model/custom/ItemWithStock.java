package com.blibli.future.pos.restaurant.common.model.custom;

import com.blibli.future.pos.restaurant.common.model.BaseResource;

import java.util.HashMap;
import java.util.Map;

public class ItemWithStock extends BaseResource {
    private Integer itemId;
    private String itemName;
    private Integer restaurantId;
    private String restaurantAddress;
    private Integer stock;

    public ItemWithStock() {
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

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ItemWithStock{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", restaurantId=" + restaurantId +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", stock=" + stock +
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return (notValidAttribute());
    }

    @Override
    public Boolean notValidAttribute() {
        return (itemId == null || restaurantId == null || stock == null || stock < 0);
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(restaurantId == null) required.put("restaurantId", "Integer");
        if(itemId == null) required.put("itemId", "Integer");
        if(stock == null) required.put("stock", "Integer");
        return required;
    }
}
