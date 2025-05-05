package org.example.common.commands;

import org.example.client.CityFactory;
import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;

import static org.example.client.CityFactory.updateCityFields;


public class UpdateCommand implements Command {
    private final long id;
    private final City updatedCity;

    public UpdateCommand(long id, City updatedCity, CityFactory cityFactory) {
        this.id = id;
        this.updatedCity = updatedCity;
    }

    @Override
    public Response execute(CityCollection cityCollection) {
        City cityToUpdate = cityCollection.getById(id);

        if (cityToUpdate == null) {
            return new Response("ERROR", "Город с ID=" + id + " не найден");
        }

        updateCityFields(updatedCity);
        return new Response("OK", "Город с ID=" + id + " успешно обновлен");
    }
}