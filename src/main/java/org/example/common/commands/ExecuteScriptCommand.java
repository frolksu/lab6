package org.example.common.commands;


import org.example.common.Response;
import org.example.server.CityCollection;

import java.util.List;

public class ExecuteScriptCommand implements Command {

    @Override
    public Response execute(CityCollection cityCollection, List<Command> commands) {
        commands.forEach(cmd -> cmd.execute(cityCollection));
        return new Response("OK", "Скрипт выполнен");
    }
}