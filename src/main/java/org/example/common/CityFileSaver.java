package org.example.common;


import org.example.common.model.entity.City;
import org.example.server.CityCollection;

import java.io.FileWriter;
import java.io.IOException;

public class CityFileSaver {
    public static void saveCities(CityCollection collection, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (City city : collection.getCities()) {
                writer.write(convertToCsv(city) + "\n");
            }
            System.out.println("Сохранено городов: " + collection.getCities().size());
        } catch (IOException e) {
            System.err.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    private static String convertToCsv(City city) {
        return String.join(",",
                String.valueOf(city.getId()),
                city.getName(),
                String.valueOf(city.getCoordinates().getX()),
                String.valueOf(city.getCoordinates().getY()),
                String.valueOf(city.getArea()),
                String.valueOf(city.getPopulation()),
                city.getMetersAboveSeaLevel() != null ?
                        String.valueOf(city.getMetersAboveSeaLevel()) : "",
                String.valueOf(city.getCarCode()),
                city.getAgglomeration() != null ?
                        String.valueOf(city.getAgglomeration()) : "",
                city.getStandardOfLiving().name(),
                city.getGovernor() != null ?
                        city.getGovernor().getName() : ""
        );
    }
}