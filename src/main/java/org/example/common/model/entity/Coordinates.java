package org.example.common.model.entity;

import java.io.Serializable;

public class Coordinates implements  Serializable {
    private Float y;
    private Float x;

    public Coordinates(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public String toCSV() {
        return x + "," + y;
    }


    public Object getX() {
        return x;
    }

    public Object getY() {
        return y;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(Float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
