package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class RemoveByIdCommand implements Command {


    @Override
    public Response execute(CityCollection cityCollection, long id) {
        boolean removed = cityCollection.removeById(id);
        return new Response("OK", removed ? "Удалено" : "Город не найден");
    }
}