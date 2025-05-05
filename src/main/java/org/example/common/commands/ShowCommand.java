package org.example.common.commands;

import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;

import java.util.Collection;


public class ShowCommand implements Command {
    @Override
    public Response execute(CityCollection cityCollection) {
        Collection<City> cities = cityCollection.getCities();

        return new Response("OK", cities == null || cities.isEmpty() ? "Коллекция пуста" : cities);
    }
}