package com.blibli.future.pos.restaurant.common.model;


import java.sql.Timestamp;

public class Member {
    private int id;
    private Timestamp timestampCreated;
    private String name;
    private String address;
    private String email;
    private String href;

    public Member() {
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

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    /**
     * Must trigger after set all variable
     */
    public void autoSetHref() {
        this.href = "/members/" + id;
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
}
