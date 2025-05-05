package org.example.server;

import org.example.common.model.collection.IdGenerator;
import org.example.common.model.entity.City;
import org.example.common.model.entity.Coordinates;
import org.example.common.model.entity.Human;
import org.example.common.model.entity.StandardOfLiving;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CityFileLoader {

    public static void loadCities(CityCollection collection, String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                City city = parseCity(line);
                if (city != null) {
                    collection.add(city);
                    IdGenerator.updateId(city.getId());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + filename);
        }
        System.out.println("Загружено городов: " + collection.getCities().size());
    }
    private static StandardOfLiving parseStandardOfLiving(String input) {
        if (input == null || input.isEmpty()) {
            return StandardOfLiving.ULTRA_LOW;
        }
        return StandardOfLiving.valueOf(input.toUpperCase());
    }

    private static City parseCity(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            return new City(
                    Long.parseLong(parts[0].trim()),       // id (long)
                    parts[1].trim(),                       // name (String)
                    new Coordinates(                       // coordinates
                            Float.parseFloat(parts[2].trim()),
                            Float.parseFloat(parts[3].trim())
                    ),
                    Float.valueOf(parts[4].trim()),        // area (Float)
                    Integer.parseInt(parts[5].trim()),     // population (int)
                    parts[6].isEmpty() ? null : Float.valueOf(parts[6].trim()), // metersAboveSeaLevel
                    Integer.valueOf(parts[7].trim()),      // carCode (Integer)
                    parts[8].isEmpty() ? null : Long.valueOf(parts[8].trim()),  // agglomeration
                    parseStandardOfLiving(parts[9].trim()), // standardOfLiving
                    parts[10].isEmpty() ? null : new Human(parts[10].trim())    // governor
            );
        } catch (Exception e) {
            System.err.println("Ошибка парсинга строки: " + csvLine);
            return null;
        }
    }
}