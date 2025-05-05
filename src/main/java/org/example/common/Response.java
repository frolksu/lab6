package org.example.common;

import java.io.Serializable;

/**
 * Ответ сервера клиенту.
 */

public class Response implements Serializable {
    private final String message;
    private final Object data;

    public Response(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() { return message; }
    public Object getData() { return data; }
}
