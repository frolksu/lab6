package org.example.common.commands;

import org.example.common.Response;
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
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                Response response = processCommand(line);
                if ("ERROR".equals(response.getMessage())) {
                    return response;
                }
            }
            return new Response("OK", "Скрипт выполнен успешно");
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

    private Command createCommand(String cmd, String args) {
        switch (cmd) {
            case "add":
                return new AddCommand(args);
            case "remove_by_id":
                return new RemoveByIdCommand(Long.parseLong(args));
            case "help":
                return new HelpCommand();
            case "info":
                return new InfoCommand();
            case "show":
                return new ShowCommand();
            case "clear":
                return new ClearCommand();
            case "exit":
                return new ExitCommand();
            case "history":
                return new HistoryCommand();
            case "min_by_governor":
                return new MinByGovernorCommand();
            default: throw new IllegalArgumentException("Неизвестная команда: " + cmd);
        }
    }
}