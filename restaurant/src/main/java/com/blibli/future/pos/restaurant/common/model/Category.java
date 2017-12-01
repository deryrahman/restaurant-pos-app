package com.blibli.future.pos.restaurant.common.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement
public class Category {
    private int id;
    private Timestamp timestampCreated;
    private String name;
    private String description;
    private String href;
    private String hrefItems;

    public Category() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHrefItems() {
        return hrefItems;
    }

    public void setHrefItems(String hrefItems) {
        this.hrefItems = hrefItems;
    }

    /**
     * Must trigger after set all variable
     */
    public void autoSetHref(){
        this.href = "/categories/" + id;
        this.hrefItems = this.href + "/items";
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", timestampCreated=" + timestampCreated +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", href='" + href + '\'' +
                ", hrefItems='" + hrefItems + '\'' +
                '}';
    }
}
