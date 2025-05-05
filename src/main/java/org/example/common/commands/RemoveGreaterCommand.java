package org.example.common.commands;


import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;

import java.util.Comparator;

public class RemoveGreaterCommand implements Command {
    private  City city;
    private Comparator<City> comparator;

    public RemoveGreaterCommand(City city, Comparator<City> comparator) {
        this.city = city;
        this.comparator = comparator;
    }

    @Override
    public Response execute(CityCollection cityCollection) {
        int removed = cityCollection.removeGreater(city, comparator);
        return new Response("OK", "Удалено городов: " + removed);
    }
}