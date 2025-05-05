package org.example.common.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class City implements Comparable<City>, Serializable {
    long id;
    String name;
    Coordinates coordinates;
    LocalDate creationDate;
    Float area;
    int population;
    Float metersAboveSeaLevel;
    Integer carCode;
    Long agglomeration;
    StandardOfLiving standardOfLiving;
    Human governor;

    public City(long id, String name, Coordinates coordinates, Float area, int population,
                Float metersAboveSeaLevel, Integer carCode, Long agglomeration,
                StandardOfLiving standardOfLiving, Human governor) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Название города не может быть null.");
        this.coordinates = Objects.requireNonNull(coordinates, "Координаты не могут быть null.");
        this.creationDate = LocalDate.now();
        this.area = Objects.requireNonNull(area, "Площадь не может быть null.");
        this.population = Objects.requireNonNull(population, "Население не может быть null.");
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.carCode = Objects.requireNonNull(carCode, "Код автомобиля не может быть null.");
        this.agglomeration = agglomeration;
        this.standardOfLiving = standardOfLiving;
        this.governor = Objects.requireNonNull(governor, "Имя губернатора не может быть null.");
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public Float getArea() { return area; }
    public int getPopulation() { return population; }
    public Float getMetersAboveSeaLevel() { return metersAboveSeaLevel; }
    public Integer getCarCode() { return carCode; }
    public Long getAgglomeration() { return agglomeration; }
    public StandardOfLiving getStandardOfLiving() { return standardOfLiving; }
    public Human getGovernor() { return governor; }

    public void setName(String name) {
        this.name = name;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setMetersAboveSeaLevel(Float metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
    }

    public void setCarCode(Integer carCode) {
        this.carCode = carCode;
    }

    public void setAgglomeration(Long agglomeration) {
        this.agglomeration = agglomeration;
    }

    public void setStandardOfLiving(StandardOfLiving standardOfLiving) {
        this.standardOfLiving = standardOfLiving;
    }


    @Override
    public int compareTo(City other) {
        return Long.compare(this.id, other.id);
    }

    public String toCSV() {
        return String.join(",",
                String.valueOf(id),
                name,
                coordinates.toCSV(),
                creationDate.toString(),
                String.valueOf(area),
                String.valueOf(population),
                String.valueOf(metersAboveSeaLevel),
                String.valueOf(carCode),
                String.valueOf(agglomeration),
                String.valueOf(standardOfLiving),
                governor != null ? governor.getName() : ""
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {

        return String.format(
                "Город: %s\n" +
                        "• ID: %d\n" +
                        "• Координаты: (широта: %.1f, долгота: %.1f)\n" +
                        "• Дата основания: %s\n" +
                        "• Площадь: %.1f кв.км\n" +
                        "• Население: %,d чел.\n" +
                        "• Высота над уровнем моря: %.1f м\n" +
                        "• Автомобильный код: %d\n" +
                        "• Агломерация: %s\n" +
                        "• Уровень жизни: %s\n" +
                        "• Губернатор: %s\n",
                name, id, coordinates.getX(), coordinates.getY(),
                creationDate.format(DateTimeFormatter.ISO_DATE),
                area, population, metersAboveSeaLevel,
                carCode,
                agglomeration != null ? String.format("%,d", agglomeration) : "нет данных",
                standardOfLiving,
                governor
        );
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGovernor(Human governor) {
        this.governor = governor;
    }
}
