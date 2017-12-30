package com.blibli.future.pos.restaurant.common.model;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Item extends BaseResource {
    private Integer id;
    private Timestamp timestampCreated;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer categoryId;
    private String status;

    public Item() {
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return (id == null || notValidAttribute());
    }

    @Override
    public Boolean notValidAttribute() {
        return (name == null || price == null || status == null || categoryId == null || name.isEmpty() || status.isEmpty() || price.compareTo(BigDecimal.ZERO) < 0);
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(name == null || name.isEmpty()) required.put("name", "String");
        if(price == null) required.put("price", "Decimal");
        if(categoryId == null) required.put("categoryId", "Integer");
        if(status == null || status.isEmpty()) required.put("status", "String");
        return required;
    }
}
