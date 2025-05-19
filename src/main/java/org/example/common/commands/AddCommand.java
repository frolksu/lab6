package org.example.common.commands;

import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;

public class AddCommand implements Command {
    private final City city;

    public AddCommand(City city) {
        this.city = city;
    }

    @Override
    public Response execute(CityCollection collection) {
        if (city == null) {
            return new Response("ERROR", "Не удалось создать город");
        }

        collection.add(city);
        return new Response("OK", "Город добавлен (ID: " + city.getId() + ")");
    }
}