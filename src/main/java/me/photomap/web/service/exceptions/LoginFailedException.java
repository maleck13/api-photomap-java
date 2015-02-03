package me.photomap.web.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class LoginFailedException extends AuthenticationFailure {
    public LoginFailedException(String message) {
        super(message);
    }
}
