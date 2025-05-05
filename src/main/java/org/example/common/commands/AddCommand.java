package org.example.common.commands;


import org.example.client.CityFactory;
import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;
import org.example.server.CityParser;

import java.util.Scanner;

public class AddCommand implements Command {
    private final CityCollection cityCollection;
    private final String csvLine;

    public AddCommand(CityCollection cityCollection) {
        this.cityCollection = cityCollection;
        this.csvLine = null;
    }

    public AddCommand(CityCollection cityCollection, String csvLine) {
        this.cityCollection = cityCollection;
        this.csvLine = csvLine;
    }

    @Override
    public Response execute(CityCollection cityCollection) {
        City city;
        if (csvLine != null) {
            city = CityParser.parseFromCSV(csvLine);
        } else {
            city = CityFactory.createCity();
        }
        cityCollection.add(city);
        return new Response("OK", "Город добавлен");
    }
}
