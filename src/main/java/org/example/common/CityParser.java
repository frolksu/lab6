package org.example.common;


import org.example.common.model.collection.IdGenerator;
import org.example.common.model.entity.City;
import org.example.common.model.entity.Coordinates;
import org.example.common.model.entity.Human;
import org.example.common.model.entity.StandardOfLiving;

public class CityParser {
    public static City parseFromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            return new City(
                    IdGenerator.getNextId(),
                    parts[0].trim(),
                    new Coordinates(
                            Float.parseFloat(parts[1].trim()),
                            Float.parseFloat(parts[2].trim())
                    ),
                    Float.parseFloat(parts[3].trim()),
                    Integer.parseInt(parts[4].trim()),
                    parts.length > 5 ? Float.parseFloat(parts[5].trim()) : null,
                    Integer.parseInt(parts[6].trim()),
                    parts.length > 7 ? Long.parseLong(parts[7].trim()) : null,
                    parts.length > 8 ? StandardOfLiving.valueOf(parts[8].trim()) : null,
                    parts.length > 9 ? new Human(parts[9].trim()) : null
            );
        } catch (Exception e) {
            System.err.println("Ошибка парсинга города: " + e.getMessage());
            return null;
        }
    }
}
