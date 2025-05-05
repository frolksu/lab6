package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class RemoveAnyByGovernorCommand implements Command {
    @Override
    public Response execute(CityCollection collection, String governorName) {
        boolean removed = collection.removeAnyByGovernor(governorName);
        return new Response("OK", removed ? "Удалено" : "Не найдено");
    }
}

