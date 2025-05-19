package org.example.common.commands;

import org.example.common.Response;
import org.example.common.model.entity.City;
import org.example.common.model.entity.Coordinates;
import org.example.common.model.entity.Human;
import org.example.common.model.entity.StandardOfLiving;
import org.example.server.CityCollection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ExecuteScriptCommand implements Command {
    private static final Set<String> executingScripts = new HashSet<>();
    private final String filename;
    private final CityCollection collection;

    public ExecuteScriptCommand(String filename, CityCollection collection) {
        this.filename = filename;
        this.collection = collection;
    }

    @Override
    public Response execute(CityCollection collection) {
        if (executingScripts.contains(filename)) {
            return new Response("ERROR", "Обнаружена рекурсия в файле: " + filename);
        }

        File file = new File(filename);
        if (!file.exists() || !file.canRead()) {
            return new Response("ERROR", "Файл не существует или недоступен: " + filename);
        }

        executingScripts.add(filename);
        Response result = processScriptFile(file);
        executingScripts.remove(filename);

        return result;
    }

    private Response processScriptFile(File file) {
        StringBuilder output = new StringBuilder();
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                Response response = processCommand(line);
                if ("ERROR".equals(response.getMessage())) {
                    return response;
                }
                output.append(response.getData()).append("\n");
            }
            return new Response("OK", output.toString());
        } catch (IOException e) {
            return new Response("ERROR", "Ошибка чтения файла: " + e.getMessage());
        }
    }

    private Response processCommand(String commandLine) {
        String[] parts = commandLine.split(" ", 2);
        String cmd = parts[0];
        String args = parts.length > 1 ? parts[1] : "";

        if ("execute_script".equals(cmd)) {
            return new ExecuteScriptCommand(args, collection).execute(collection);
        }

        try {
            Command command = createCommand(cmd, args);
            return command.execute(collection);
        } catch (Exception e) {
            return new Response("ERROR", "Ошибка выполнения: " + commandLine + " - " + e.getMessage());
        }
    }

    private Command createCommand(String cmd, String args) throws Exception {
        switch (cmd) {
            case "help":
                return new HelpCommand();
            case "info":
                return new InfoCommand();
            case "show":
                return new ShowCommand();
            case "add":
                return new AddCommand(parseCityFromArgs(args));
            case "update":
                return new UpdateCommandWrapper(parseId(args), parseCityFromArgs(args));
            case "remove_by_id":
                return new RemoveByIdCommand(parseId(args));
            case "clear":
                return new ClearCommand();
            case "exit":
                return new ExitCommand();
            case "add_if_min":
                return new AddIfMinCommand(parseCityFromArgs(args));
            case "remove_greater":
                return new RemoveGreaterCommand(parseCityFromArgs(args));
            case "history":
                return new HistoryCommand();
            case "remove_any_by_governor":
                return new RemoveAnyByGovernorCommand(args);
            case "min_by_governor":
                return new MinByGovernorCommand();
            case "count_less_than_standard_of_living":
                return new CountLessThanStandardOfLivingCommand(
                        StandardOfLiving.valueOf(args.toUpperCase()));
            default:
                throw new IllegalArgumentException("Неизвестная команда: " + cmd);
        }
    }

    private long parseId(String args) throws NumberFormatException {
        return Long.parseLong(args.split(" ")[0]);
    }

    private City parseCityFromArgs(String args)  {
        String[] parts = args.split(";");
        if (parts.length < 10) {
            throw new IllegalArgumentException("Недостаточно параметров для создания города");
        }

        return new City(
                0,
                parts[0], // name
                new Coordinates(
                        Float.parseFloat(parts[1]), // x
                        Float.parseFloat(parts[2])  // y
                ),
                Float.parseFloat(parts[3]), // area
                Integer.parseInt(parts[4]), // population
                Float.parseFloat(parts[5]), // metersAboveSeaLevel
                Integer.parseInt(parts[6]), // carCode
                parts[7].isEmpty() ? null : Long.parseLong(parts[7]), // agglomeration
                StandardOfLiving.valueOf(parts[8].toUpperCase()), // standardOfLiving
                parts[9].isEmpty() ? null : new Human(parts[9]) // governor
        );
    }
}