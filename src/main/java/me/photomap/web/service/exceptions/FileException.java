package me.photomap.web.service.exceptions;

import java.io.IOException;

/**
 * Created by craigbrookes on 21/12/14.
 */
public class FileException extends IOException {
    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
