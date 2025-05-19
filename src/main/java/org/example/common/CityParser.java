package org.example.common;


import org.example.common.model.entity.City;
import org.example.common.model.entity.Coordinates;
import org.example.common.model.entity.Human;
import org.example.common.model.entity.StandardOfLiving;

public class CityParser {
    public static City parseFromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            return new City(
                    Long.parseLong(parts[0].trim()),
                    parts[1].trim(),
                    new Coordinates(
                            Float.parseFloat(parts[2].trim()),
                            Float.parseFloat(parts[3].trim())
                    ),
                    Float.parseFloat(parts[4].trim()),
                    Integer.parseInt(parts[5].trim()),
                    parts.length > 6 ? Float.parseFloat(parts[6].trim()) : null,
                    Integer.parseInt(parts[7].trim()),
                    parts.length > 8 ? Long.parseLong(parts[8].trim()) : null,
                    parts.length > 9 ? StandardOfLiving.valueOf(parts[9].trim()) : null,
                    parts.length > 10 ? new Human(parts[10].trim()) : null
            );
        } catch (Exception e) {
            System.err.println("Ошибка парсинга города: " + e.getMessage());
            return null;
        }
    }
}
