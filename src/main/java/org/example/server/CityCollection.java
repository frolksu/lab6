package org.example.server;


import org.example.common.model.entity.City;
import org.example.common.model.entity.StandardOfLiving;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class CityCollection {
    private final TreeSet<City> cities;
    private final LocalDate initializationDate;
    private long nextId = 1;

    public CityCollection() {
        cities = new TreeSet<>();
        initializationDate = LocalDate.now();
    }

    public TreeSet<City> getCities() {
        return cities;
    }

    public LocalDate getInitializationDate() {
        return initializationDate;
    }

    public synchronized void add(City city) {
        if (city.getId() == 0) {

            city.setId(nextId++);
        } else {

            if (city.getId() >= nextId) {
                nextId = city.getId() + 1;
            }
        }
        cities.add(city);
    }

    public synchronized boolean removeById(long id) {
        return cities.removeIf(city -> city.getId() == id);
    }

    public synchronized void clear() {
        cities.clear();
    }

    public synchronized City getById(long id) {
        for (City city : cities) {
            if (city.getId() == id) {
                return city;
            }
        }
        return null;
    }

    public synchronized City getMin() {
        return cities.isEmpty() ? null : cities.first();
    }

    public synchronized int removeGreater(City city, Comparator<City> comparator) {
        int initialSize = cities.size();
        cities.removeIf(c -> comparator.compare(c, city) > 0);
        return initialSize - cities.size();
    }

    public synchronized boolean removeAnyByGovernor(String governorName) {
        Iterator<City> iterator = cities.iterator();
        while (iterator.hasNext()) {
            City c = iterator.next();
            if (c.getGovernor() != null && c.getGovernor().getName().equals(governorName)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public synchronized City getMinByGovernor() {
        City minCity = null;
        for (City c : cities) {
            if (c.getGovernor() != null) {
                if (minCity == null || c.getGovernor().getName().compareTo(minCity.getGovernor().getName()) < 0) {
                    minCity = c;
                }
            }
        }
        return minCity;
    }


    public synchronized long countLessThanStandardOfLiving(StandardOfLiving standard) {
        return cities.stream()
                .filter(c -> c.getStandardOfLiving() != null)
                .filter(c -> c.getStandardOfLiving().isLessThan(standard))
                .count();
    }
}
