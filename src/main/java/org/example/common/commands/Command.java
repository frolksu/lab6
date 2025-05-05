package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

import java.io.Serializable;

public interface Command extends Serializable {
    Response execute(CityCollection collection);
}