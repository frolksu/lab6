package org.example.server;


import org.example.common.model.entity.City;
import java.io.*;
import java.util.logging.*;

public class CityFileSaver {
    private static final Logger logger = Logger.getLogger(CityFileSaver.class.getName());

    public static void saveCities(CityCollection collection, String filename) throws IOException {
        File file = new File(filename);
        try (PrintWriter writer = new PrintWriter(file)) {
            collection.getCities().forEach(city -> {
                writer.println(convertToCSV(city));
            });
            logger.info("Сохранено " + collection.getCities().size() + " городов");
        }
    }

    private static String convertToCSV(City city) {
        return String.join(",",
                String.valueOf(city.getId()),
                city.getName(),
                String.valueOf(city.getCoordinates().getX()),
                String.valueOf(city.getCoordinates().getY()),
                String.valueOf(city.getArea()),
                String.valueOf(city.getPopulation()),
                city.getMetersAboveSeaLevel() != null ? String.valueOf(city.getMetersAboveSeaLevel()) : "",
                String.valueOf(city.getCarCode()),
                city.getAgglomeration() != null ? String.valueOf(city.getAgglomeration()) : "",
                city.getStandardOfLiving() != null ? city.getStandardOfLiving().name() : "",
                city.getGovernor() != null ? city.getGovernor().getName() : ""
        );
    }
}
