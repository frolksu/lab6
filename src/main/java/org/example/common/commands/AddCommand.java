package org.example.common.commands;


import org.example.client.CityFactory;
import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;

public class AddCommand implements Command {
    @Override
    public Response execute(CityCollection collection) {
        City city = CityFactory.createCity();
        collection.add(city);
        return new Response("OK", "Город добавлен");
        }
}

