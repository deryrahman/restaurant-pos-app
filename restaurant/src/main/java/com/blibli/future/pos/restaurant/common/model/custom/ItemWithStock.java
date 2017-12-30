package com.blibli.future.pos.restaurant.common.model.custom;

import com.blibli.future.pos.restaurant.common.model.BaseResource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ItemWithStock extends BaseResource {
    private Integer itemId;
    private String itemName;
    private Integer restaurantId;
    private String restaurantAddress;
    private Integer stock;
    private BigDecimal price;
    private Integer categoryId;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "ItemWithStock{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", restaurantId=" + restaurantId +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", categoryId=" + categoryId +
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
