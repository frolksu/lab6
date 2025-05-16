package org.example.common;

import java.io.Serializable;

/**
 * Ответ сервера клиенту.
 */

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private Object data;

    public Response(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }
}