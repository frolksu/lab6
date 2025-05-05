package org.example.common.commands;

import org.example.common.Response;
import org.example.common.model.entity.StandardOfLiving;
import org.example.server.CityCollection;

public class CountLessThanStandardOfLivingCommand implements Command {
    private final StandardOfLiving standard;

    public CountLessThanStandardOfLivingCommand(StandardOfLiving standard) {
        this.standard = standard;
    }

    public CountLessThanStandardOfLivingCommand(String csvLine) {
        String[] parts = csvLine.split(",");
        this.standard = StandardOfLiving.valueOf(parts[0].trim().toUpperCase());
    }

    @Override
    public Response execute(CityCollection collection) {
        long count = collection.countLessThanStandardOfLiving(standard);
        return new Response("OK", "Количество элементов: " + count);
    }
}