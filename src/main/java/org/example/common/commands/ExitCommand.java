package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class ExitCommand implements Command {
    @Override
    public Response execute(CityCollection collection) {
        return new Response("EXIT", "Завершение работы");
    }
}
