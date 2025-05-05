package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class RemoveAnyByGovernorCommand implements Command {
    private String governorName;

    @Override
    public Response execute(CityCollection collection) {
        boolean removed = collection.removeAnyByGovernor(governorName);
        return new Response("OK", removed ? "Удалено" : "Не найдено");
    }
}

