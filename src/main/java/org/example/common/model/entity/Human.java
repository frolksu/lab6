package org.example.common.model.entity;

import java.io.Serializable;

public class Human implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    public Human(String governorName) {
        this.name = governorName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}