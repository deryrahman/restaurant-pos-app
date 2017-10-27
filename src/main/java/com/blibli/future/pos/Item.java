package com.blibli.future.pos;

import java.math.BigInteger;

/**
 * Created by dery on 10/27/17.
 */
public class Item {
    Long id;
    String name;
    String price;

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
}
