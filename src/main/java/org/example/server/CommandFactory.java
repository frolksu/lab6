package org.example.server;

import org.example.client.CityFactory;
import org.example.common.Request;
import org.example.common.commands.*;
import org.example.common.model.entity.City;
import org.example.common.model.entity.StandardOfLiving;


public class CommandFactory {
    private static CityCollection collection;

    public static void init(CityCollection cityCollection) {
        collection = cityCollection;
    }

    public static Command createCommand(Request request) {
        if (request == null || request.getCommandName() == null) {
            throw new IllegalArgumentException("Пустой запрос");
        }

        if (collection == null) {
            throw new IllegalStateException("CommandFactory не инициализирован. Вызовите init() перед использованием");
        }

        String commandName = request.getCommandName().toLowerCase();

        try {
            switch (commandName) {
                case "help": return new HelpCommand();
                case "info": return new InfoCommand();
                case "show": return new ShowCommand();
                case "clear": return new ClearCommand();
                case "exit": return new ExitCommand();
                case "history": return new HistoryCommand();
                case "min_by_governor": return new MinByGovernorCommand();

                case "add":
                    Object addData = request.getData();
                    if (addData instanceof City) {
                        return new AddCommand((City) addData);
                    }
                    return new AddCommand(addData.toString());

                case "update":
                    Object[] updateArgs = (Object[]) request.getData();
                    return new UpdateCommand((Long) updateArgs[0], (City) updateArgs[1]);

                case "remove_by_id":
                    return new RemoveByIdCommand((Long) request.getData());

                case "add_if_min":
                    City city = request.getData() instanceof City
                            ? (City) request.getData()
                            : CityFactory.createCity();
                    return new AddIfMinCommand(city);

                case "remove_greater":
                    return new RemoveGreaterCommand((City) request.getData());

                case "remove_any_by_governor":
                    return new RemoveAnyByGovernorCommand(request.getData().toString());

                case "count_less_than_standard_of_living":
                    Object solData = request.getData();
                    if (solData instanceof StandardOfLiving) {
                        return new CountLessThanStandardOfLivingCommand((StandardOfLiving) solData);
                    }
                    return new CountLessThanStandardOfLivingCommand(solData.toString());

                case "execute_script":
                    return new ExecuteScriptCommand(request.getData().toString(), collection);

                default:
                    throw new IllegalArgumentException("Неизвестная команда: " + commandName);
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Неверные аргументы для команды " + commandName);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания команды: " + e.getMessage(), e);
        }
    }
}