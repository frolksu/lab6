package org.example.common.commands;

import org.example.client.CityFactory;
import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;
import org.example.common.CityParser;

public class AddCommand implements Command {
    private final City city;

    public AddCommand(String csvLine) {
        this.city = CityParser.parseFromCSV(csvLine);
    }

    public AddCommand(City city) {
        this.city = city;
    }

    @Override
    public Response execute(CityCollection collection) {
        City cityToAdd = this.city;

        if (cityToAdd == null) {
            cityToAdd = CityFactory.createCity();
        }

        if (cityToAdd == null) {
            return new Response("ERROR", "Не удалось создать город");
        }

        collection.add(cityToAdd);
        return new Response("OK", "Город добавлен (ID: " + cityToAdd.getId() + ")");
    }
}