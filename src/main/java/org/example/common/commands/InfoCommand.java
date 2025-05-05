package org.example.common.commands;

import org.example.common.Response;
import org.example.server.CityCollection;

public class InfoCommand implements Command {
    @Override
    public Response execute(CityCollection cityCollection) {
        String info = "Тип: " + cityCollection.getCities().getClass().getSimpleName() +
                ", Количество элементов: " + cityCollection.getCities().size() +
                ", Дата инициализации: " + cityCollection.getInitializationDate();
        return new Response("OK", info);
    }
}
