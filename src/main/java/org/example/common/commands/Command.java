package org.example.common.commands;


import org.example.client.CityFactory;
import org.example.common.Request;
import org.example.common.Response;
import org.example.common.model.entity.City;

import java.util.TreeSet;

public interface Command {
    Response execute(CityFactory collection, Request request) throws IllegalArgumentException;
}