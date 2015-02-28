package me.photomap.web.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by craigbrookes on 22/12/14.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationFailure extends RuntimeException {

  public AuthenticationFailure(String message) {
    super(message);
  }
}
