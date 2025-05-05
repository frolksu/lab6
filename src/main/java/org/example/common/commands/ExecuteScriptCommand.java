package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

import java.util.List;

public class ExecuteScriptCommand implements Command {
    private List<Command> commands;

    public ExecuteScriptCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public Response execute(CityCollection cityCollection) {
        commands.forEach(cmd -> cmd.execute(cityCollection));
        return new Response("OK", "Скрипт выполнен");
    }
}