package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class RemoveByIdCommand implements Command {
    private final long id;

    public RemoveByIdCommand(long id) {
        this.id = id;
    }

    @Override
    public Response execute(CityCollection cityCollection) {
        boolean removed = cityCollection.removeById(id);
        return new Response("OK", removed ? "Удалено" : "Город не найден");
    }
}