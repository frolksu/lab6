package org.example.common;

import java.io.Serializable;

/**
 * Ответ сервера клиенту.
 */


public class Response implements Serializable {
    private String message;
    private Object data;

    public Response(String message) {
        this.message = message;
    }

    public Response(String message, Object data) {
        this.message = message;
        this.data = data;
    }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}
