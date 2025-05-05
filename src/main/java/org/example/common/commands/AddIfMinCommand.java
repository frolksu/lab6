package org.example.common.commands;

import org.example.client.CityFactory;
import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;

public class AddIfMinCommand implements Command {
    private final City city;

    public AddIfMinCommand(City city) {
        this.city = city;
    }

    @Override
    public Response execute(CityCollection cityCollection) {
        City city = CityFactory.createCity();
        City minCity = cityCollection.getMin();
        if (minCity == null || city.compareTo(minCity) < 0) {
            cityCollection.add(city);
            return new Response("OK", "Город добавлен");
        }
        return new Response("ERROR", "Город не является минимальным");
    }
}