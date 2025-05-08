package org.example.common;

import java.io.Serializable;

/**
 * Запрос от клиента к серверу.
 */

public class Request implements Serializable {
    private String commandName;
    private Object argument;

    public Request(String commandName, Object argument) {
        this.commandName = commandName;
        this.argument = argument;
    }

    public String getCommandName() {
        return commandName;
    }

    public Object getArgument() {
        return argument;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public void setArgument(Object argument) {
        this.argument = argument;
    }
}