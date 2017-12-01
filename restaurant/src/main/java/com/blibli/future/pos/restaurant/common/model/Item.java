package com.blibli.future.pos.restaurant.common.model;


import java.math.BigDecimal;
import java.sql.Timestamp;

public class Item {
    private int id;
    private Timestamp timestampCreated;
    private String name;
    private BigDecimal price;
    private String description;
    private int categoryId;
    private String status;
    private String href;
    private String href2;
    // special for restaurant
    private int restaurantId;
    private int stock;
    // special for receipt
    private int count;

    public Item() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Must trigger after set all variable
     */
    public void autoSetHref(){
        this.href = "/items/" + id;
        this.href2 = "/categories/" + categoryId + "/items/" + id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", timestampCreated=" + timestampCreated +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", status='" + status + '\'' +
                ", href='" + href + '\'' +
                ", href2='" + href2 + '\'' +
                '}';
    }
}
