package com.blibli.future.pos.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dery on 10/27/17.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Item {
    Long id;
    Date createdDate;
    String name;
    String price;
    Long categoryId;
    String description;
    String status;

    public Item(){}

    public Item(Long id) {
        this.id = id;
    }

    public Item(Long id, String name, String price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        Date createdDateNew = null;
        try {
            createdDateNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.createdDate = createdDateNew;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
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
        return "ID : "+id+"\n"+
                "Datetime : "+createdDate+"\n"+
                "Name : "+name+"\n"+
                "Price : "+price+"\n"+
                "Description : "+description+"\n"+
                "CategoryId : "+categoryId+"\n"+
                "Status : "+status+"\n";
    }
}
