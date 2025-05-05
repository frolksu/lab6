package org.example.common.commands;


import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;

public class MinByGovernorCommand implements Command {
    @Override
    public Response execute(CityCollection cityCollection) {
        City minCity = cityCollection.getMinByGovernor();
        if (minCity!=null) {
            return new Response("OK", minCity);
        }
        else {
            return new Response("ERROR", "Коллекция пуста.");
        }
    }
}

