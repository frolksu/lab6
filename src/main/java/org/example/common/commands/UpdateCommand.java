package org.example.common.commands;

import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.server.CityCollection;



public class UpdateCommand implements Command {
    private final City updatedCity;

    public UpdateCommand(City updatedCity) {
        this.updatedCity = updatedCity;
    }

    @Override
    public Response execute(CityCollection collection) {
        City existing = collection.getById(updatedCity.getId());
        if (existing == null) {
            return new Response("ERROR", "Город с ID=" + updatedCity.getId() + " не найден");
        }

        if (updatedCity.getName() != null) existing.setName(updatedCity.getName());
        if (updatedCity.getCoordinates() != null) existing.setCoordinates(updatedCity.getCoordinates());
        if (updatedCity.getArea() != null) existing.setArea(updatedCity.getArea());
        if (updatedCity.getPopulation() != null) existing.setPopulation(updatedCity.getPopulation());
        if (updatedCity.getMetersAboveSeaLevel() != null) existing.setMetersAboveSeaLevel(updatedCity.getMetersAboveSeaLevel());
        if (updatedCity.getCarCode() != null) existing.setCarCode(updatedCity.getCarCode());
        if (updatedCity.getAgglomeration() != null) existing.setAgglomeration(updatedCity.getAgglomeration());
        if (updatedCity.getStandardOfLiving() != null) existing.setStandardOfLiving(updatedCity.getStandardOfLiving());
        if (updatedCity.getGovernor() != null) existing.setGovernor(updatedCity.getGovernor());

        return new Response("OK", "Город с ID=" + updatedCity.getId() + " успешно обновлен");
    }
}