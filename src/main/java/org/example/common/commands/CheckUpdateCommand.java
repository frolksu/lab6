package org.example.common.commands;

import org.example.common.Request;
import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;

public class CheckUpdateCommand implements Command {
    private final long id;

    public CheckUpdateCommand(long id) {
        this.id = id;
    }

    @Override
    public Response execute(CityCollection collection) {
        City existing = collection.getById(id);
        if (existing == null) {
            return new Response("ERROR", "Город с ID=" + id + " не найден");
        }
        return new Response("OK", new UpdateCommand(id, existing));
    }
}