package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class RemoveAnyByGovernorCommand implements Command {
    private final String governorName;

    public RemoveAnyByGovernorCommand(String governorName) {
        this.governorName = governorName;
    }

    @Override
    public Response execute(CityCollection collection) {
        boolean removed = collection.removeAnyByGovernor(governorName);
        return new Response(
                removed ? "OK" : "ERROR",
                removed ? "Город с губернатором " + governorName + " удален"
                        : "Город с губернатором " + governorName + " не найден"
        );
    }
}

