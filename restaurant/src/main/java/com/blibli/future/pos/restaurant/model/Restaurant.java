package com.blibli.future.pos.restaurant.model;


import java.sql.Timestamp;

public class Restaurant {
    private int id;
    private Timestamp timestampCreated;
    private String address;
    private String phone;
    private String href;

    public Restaurant() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Must trigger after set all variable
     */
    public void autoSetHref(){
        this.href = "/restaurant/" + id;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", timestampCreated=" + timestampCreated +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
