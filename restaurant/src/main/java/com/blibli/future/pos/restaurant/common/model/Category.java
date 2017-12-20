package com.blibli.future.pos.restaurant.common.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class Category extends BaseResource {
    private Integer id;
    private Timestamp timestampCreated;
    private String name;
    private String description;

    public Category() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", timestampCreated=" + timestampCreated +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public Boolean isEmpty() {
        return id == null;
    }

    @Override
    public Boolean notValidAttribute() {
        return name == null;
    }

    @Override
    public Map<String, String> requiredAttribute() {
        Map<String, String> required = new HashMap<>();
        if(name == null) required.put("name", "String");
        return required;
    }
}
