package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;


public class HistoryCommand implements Command {

    @Override
    public Response execute(CityCollection collection) {
        return new Response("OK", "История команд хранится на клиенте");
    }
}

