package com.blibli.future.pos.restaurant.common.model;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Member extends BaseResource {
    private Integer id;
    private Timestamp timestampCreated;
    private String name;
    private String address;
    private String email;

    public Member() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", timestampCreated=" + timestampCreated +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return id == null || notValidAttribute();
    }

    @Override
    public Boolean notValidAttribute() {
        return name == null || address == null || email == null || name.isEmpty() || address.isEmpty() || email.isEmpty();
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(name == null || name.isEmpty()) required.put("name", "String");
        if(address == null || address.isEmpty()) required.put("address", "String");
        if(email == null || email.isEmpty()) required.put("email", "String");
        return required;
    }
}
