package com.blibli.future.pos.entity;

public class Member {
    private Long ID;
    private String name;
    private String address;
    private String email;

    public Member() {
    }

    public Member(Long ID, String name, String address, String email) {
        this.ID = ID;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
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
        return "ID : " + ID + '\n' +
                "Name : " + name + '\n' +
                "Address : " + address + '\n' +
                "Email : " + email + '\n';
    }
}
