package org.example.common.model.entity;

import java.io.Serializable;

public class Human implements Serializable {
    private String name; //Поле не может быть null

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