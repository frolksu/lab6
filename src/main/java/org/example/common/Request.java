package org.example.common;

import java.io.Serializable;

/**
 * Запрос от клиента к серверу.
 */

public class Request implements Serializable {
    private String commandName;
    private Object data;

    public Request(String commandName, Object data) {
        this.commandName = commandName;
        this.data = data;
    }
    public String getCommandName() { return commandName; }
    public Object getData() { return data; }
}