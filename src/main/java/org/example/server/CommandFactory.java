package org.example.server;

import org.example.common.commands.Command;
import org.example.common.commands.*;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("help", new HelpCommand());
        commands.put("info", new InfoCommand());
        commands.put("show", new ShowCommand());
        commands.put("add", new AddCommand());
        commands.put("update", new UpdateCommand());
        commands.put("remove_by_id", new RemoveByIdCommand());
        commands.put("clear", new ClearCommand());
        commands.put("execute_script", new ExecuteScriptCommand());
        commands.put("exit", new ExitCommand());
        commands.put("add_if_min", new AddIfMinCommand());
        commands.put("remove_greater", new RemoveGreaterCommand());
        commands.put("history", new HistoryCommand());
        commands.put("remove_any_by_governor", new RemoveAnyByGovernorCommand());
        commands.put("min_by_governor", new MinByGovernorCommand());
        commands.put("count_less_than_standard_of_living", new CountLessThanStandardOfLivingCommand());

    }

    public static Command getCommand(String commandName) {
        Command cmd = commands.get(commandName.toLowerCase());
        if (cmd == null) {
            throw new IllegalArgumentException("Неизвестная команда: " + commandName + " Введите 'help' для справк.");
        }
        return cmd;
    }
}
