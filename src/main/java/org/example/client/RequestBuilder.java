package org.example.client;

import org.example.common.Request;
import org.example.common.model.entity.City;
import org.example.common.model.entity.StandardOfLiving;
import org.example.common.model.entity.Human;


public class RequestBuilder {
    public static Request buildRequest(String input) throws IllegalArgumentException {
        String[] parts = input.split(" ", 2);
        String commandName = parts[0].trim().toLowerCase();
        String argString = parts.length > 1 ? parts[1].trim() : null;

        try {
            switch (commandName) {
                case "help":
                case "info":
                case "show":
                case "clear":
                case "exit":
                case "history":
                case "min_by_governor":
                    return new Request(commandName, null);

                case "add":
                case "remove_greater":
                case "add_if_min":
                    return new Request(commandName, CityFactory.createCity());

                case "update":
                    if (argString == null) throw new IllegalArgumentException("Требуется ID");
                    long id = Long.parseLong(argString);
                    return new Request("check_update", id);

                case "remove_by_id":
                    return new Request(commandName, Long.parseLong(argString));

                case "remove_any_by_governor":
                    return new Request(commandName, new Human(argString));

                case "count_less_than_standard_of_living":
                    return new Request(commandName,
                            StandardOfLiving.valueOf(argString.toUpperCase()));

                case "execute_script":
                    return new Request(commandName, argString);
                default:
                    throw new IllegalArgumentException("Неизвестная команда: " + commandName);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка создания запроса: " + e.getMessage());
        }
    }
}