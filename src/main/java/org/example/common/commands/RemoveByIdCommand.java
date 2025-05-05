package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class RemoveByIdCommand implements Command {
    private final long id;

    public RemoveByIdCommand(long id) {
        this.id = id;
    }

    @Override
    public Response execute(CityCollection collection) {
        boolean removed = collection.removeById(this.id);
        return new Response(
                removed ? "OK" : "ERROR",
                removed ? "Город с ID " + id + " удалён"
                        : "Город с ID " + id + " не найден"
        );
    }
}