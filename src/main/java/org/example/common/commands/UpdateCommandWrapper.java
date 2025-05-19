package org.example.common.commands;

import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;

public class UpdateCommandWrapper implements Command {
    private final long id;
    private final City updatedCity;

    public UpdateCommandWrapper(long id, City updatedCity) {
        this.id = id;
        this.updatedCity = updatedCity;
    }

    @Override
    public Response execute(CityCollection collection) {
        updatedCity.setId(id);
        return new UpdateCommand(updatedCity).execute(collection);
    }
}