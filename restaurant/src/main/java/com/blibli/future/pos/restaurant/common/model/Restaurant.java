package com.blibli.future.pos.restaurant.common.model;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Restaurant extends BaseResource{
    private Integer id;
    private Timestamp timestampCreated;
    private String address;
    private String phone;

    public Restaurant() {
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

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", timestampCreated=" + timestampCreated +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return (id == null || timestampCreated == null || notValidAttribute());
    }

    @Override
    public Boolean notValidAttribute() {
        return (address == null || phone == null);
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(address == null) required.put("address", "String");
        if(phone == null) required.put("phone", "String");
        return required;
    }
}
