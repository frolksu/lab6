package org.example.common.commands;

import org.example.common.Response;
import org.example.common.model.entity.StandardOfLiving;
import org.example.server.CityCollection;

public class CountLessThanStandardOfLivingCommand implements Command {
    private StandardOfLiving standard;

    public CountLessThanStandardOfLivingCommand(StandardOfLiving standard) {
        this.standard = standard;
    }

    @Override
    public Response execute(CityCollection cityCollection) {
        long count = cityCollection.countLessThanStandardOfLiving(standard);
        return new Response("OK", count);
    }
}