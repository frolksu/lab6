package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

import java.util.Deque;

public class HistoryCommand implements Command {
    private Deque<String> history;

    public HistoryCommand(Deque<String> history) {
        this.history = history;
    }

    @Override
    public Response execute(CityCollection cityCollection) {
        return new Response("OK", "Последние команды: " + history);
    }
}
