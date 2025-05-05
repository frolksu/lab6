package org.example.common.model.collection;


import org.example.common.model.entity.City;

import java.util.Comparator;

public class CityComparators {
    public static final Comparator<City> BY_ID = Comparator.comparingLong(City::getId);
    public static final Comparator<City> BY_POPULATION = Comparator.comparingInt(City::getPopulation);
    public static final Comparator<City> BY_AREA = Comparator.comparingDouble(City::getArea);
    public static final Comparator<City> BY_GOVERNOR = Comparator.comparing(
            c -> c.getGovernor() != null ? c.getGovernor().getName() : "",
            String.CASE_INSENSITIVE_ORDER
    );
}