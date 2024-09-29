package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    private Long id;
    private String name;
    private String shortName;
    private Boolean isGrouped;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Boolean getIsGrouped() {
        return isGrouped;
    }

    public void setIsGrouped(Boolean isGrouped) {
        this.isGrouped = isGrouped;
    }
}
