package org.example.common.commands;


import org.example.common.Response;
import org.example.common.model.entity.StandardOfLiving;
import org.example.server.CityCollection;

public class CountLessThanStandardOfLivingCommand implements Command {
    @Override
    public Response execute(CityCollection cityCollection, StandardOfLiving standard) {
        long count = cityCollection.countLessThanStandardOfLiving(standard);
        return new Response("OK", count);
    }
}