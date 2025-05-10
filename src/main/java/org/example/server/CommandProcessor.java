package org.example.server;

import org.example.common.Request;
import org.example.common.Response;
import org.example.common.commands.Command;

public class CommandProcessor {
    private final CityCollection cityCollection;

    public CommandProcessor(CityCollection cityCollection) {
        this.cityCollection = cityCollection;
    }

    public Response process(Request request) {
        try {
            Command command = CommandFactory.createCommand(request);
            return command.execute(cityCollection);
        } catch (Exception e) {
            return new Response("ERROR", e.getMessage());
        }
    }
}
