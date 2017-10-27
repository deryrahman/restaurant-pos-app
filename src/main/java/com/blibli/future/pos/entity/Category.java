package com.blibli.future.pos.entity;

/**
 * Created by dery on 10/27/17.
 */
public class Category {
    Long id;
    String name;
    String description;

    public Category(){}

    public Category(Long id){
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ID : "+id+"\n"+
                "Name : "+name+"\n"+
                "Description : "+description+"\n";
    }
}
