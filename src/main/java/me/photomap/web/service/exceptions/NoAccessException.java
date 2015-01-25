package me.photomap.web.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by craigbrookes on 23/12/14.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoAccessException extends RuntimeException {
}
