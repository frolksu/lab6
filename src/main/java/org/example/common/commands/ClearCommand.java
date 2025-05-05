package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class ClearCommand implements Command {

    @Override
    public Response execute(CityCollection cityCollection) {
        cityCollection.clear();
        return new Response("OK", "Коллекция очищена");
    }
}

