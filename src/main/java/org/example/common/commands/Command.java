package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public interface Command {
    Response execute(CityCollection collection);
}