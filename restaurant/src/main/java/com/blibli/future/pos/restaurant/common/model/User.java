package com.blibli.future.pos.restaurant.common.model;


import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class User extends BaseResource{
    private Integer id;
    private Timestamp timestampCreated;
    private Integer restaurantId;
    private String email;
    private String username;
    private String name;
    private String role;
    private String password;

    public User() {
    }

    public Integer getId() {
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

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Must trigger after set all variable
     */
//    public void autoSetHref(){
//        this.href = "/users/" + id;
//        this.href2 = "/restaurants/" + restaurantId + "/users/" + id;
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", timestampCreated=" + timestampCreated +
                ", restaurantId=" + restaurantId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return (id == null || timestampCreated == null || notValidAttribute());
    }

    @Override
    public Boolean notValidAttribute() {
        return (restaurantId == null || email == null || name == null || email.isEmpty() || name.isEmpty());
    }

    @Override
    public Map<String,String> requiredAttribute(){
        Map<String, String> required = new HashMap<>();
        if(email == null || email.isEmpty()) required.put("email", "String");
        if(name == null || name.isEmpty()) required.put("name", "String");
        if(restaurantId == null) required.put("restaurantId", "Integer");
        return required;
    }
}
