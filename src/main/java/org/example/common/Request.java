package org.example.common;

import java.io.Serializable;

/**
 * Запрос от клиента к серверу.
 */

public class Request implements Serializable {
    private final String commandName;
    private final Object[] args;

    public Request(String commandName, Object... args) {
        this.commandName = commandName;
        this.args = args;
    }
    
    public String getCommandName() { return commandName; }
    public Object[] getArgs() { return args; }
}