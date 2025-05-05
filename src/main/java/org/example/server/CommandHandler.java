package org.example.server;


import org.example.common.Response;
import org.example.common.commands.Command;
import org.example.common.model.entity.City;

public class CommandHandler {
    private CityCollection cityCollection;

    public Response handle(Command command) {
        try {
            switch (request.getCommandName()) {
                case "add":
                    City city = (City) request.getArgs()[0];
                    cityCollection.add(city);
                    return new Response("OK", "Город добавлен");
        } catch (Exception e) {
            return new Response("ERROR", e.getMessage());
        }
    }
}