package org.example.common.commands;

import org.example.common.Response;
import org.example.common.model.collection.CityComparators;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;
import java.util.Comparator;

public class RemoveGreaterCommand implements Command {
    private final City city;

    public RemoveGreaterCommand(City city) {
        this.city = city;
    }

    @Override
    public Response execute(CityCollection collection) {
        int count = collection.removeGreater(city, CityComparators.BY_POPULATION);
        return new Response("OK", "Удалено городов: " + count);
    }
}