package org.example.common.model.entity;

public enum StandardOfLiving {
    ULTRA_HIGH(4),
    VERY_HIGH(3),
    LOW(2),
    ULTRA_LOW(1);

    private final int level;

    StandardOfLiving(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLessThan(StandardOfLiving other) {
        return this.level < other.level;
    }
}