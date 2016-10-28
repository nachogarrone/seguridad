package com.ucu.seguridad.security;

/**
 * Created by nachogarrone on 10/28/16.
 */
public class MessageBuilder {
    String key;
    String message;

    public MessageBuilder(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
