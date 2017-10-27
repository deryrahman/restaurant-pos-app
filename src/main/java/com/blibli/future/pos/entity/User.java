package com.blibli.future.pos.entity;

public class User {

    private Long user_id;
    private Long restaurant_id;
    private String name;
    private String password;
    private String email;
    private String role;

    public User() {
    }

    public User(Long user_id, Long restaurant_id, String name, String password, String email, String role) {
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Long restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
