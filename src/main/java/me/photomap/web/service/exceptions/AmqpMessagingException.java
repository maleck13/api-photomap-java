package me.photomap.web.service.exceptions;


import java.io.IOException;

public class AmqpMessagingException extends IOException {

    public AmqpMessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
