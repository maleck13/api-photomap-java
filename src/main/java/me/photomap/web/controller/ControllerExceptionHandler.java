package me.photomap.web.controller;

import me.photomap.web.service.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ControllerExceptionHandler {


  Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  Map handleException(AmqpMessagingException ex) {
    log.warn("error with sending message via amqp ", ex);
    Map<String, String> message = new HashMap<String, String>();
    message.put("error", ex.getMessage());
    return message;
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  Map handleException(FileNotFoundException ex) {
    Map<String, String> message = new HashMap();
    message.put("error", ex.getMessage());
    return message;
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  Map handleException(FileException ex) {
    log.warn("error with file operation ", ex);
    Map<String, String> message = new HashMap<String, String>();
    message.put("error", ex.getMessage());
    return message;
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  Map handleException(AuthenticationFailure ex) {

    Map<String, String> message = new HashMap<String, String>();
    message.put("error", ex.getMessage());
    return message;
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map handleException(org.springframework.validation.BindException ex) {
    log.error("unexpected error " + ex.getClass(), ex);
    Map errors = new HashMap();
    for (FieldError fieldError : ex.getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    Map<String, Map> message = new HashMap();
    message.put("error", errors);
    return message;
  }


  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  Map handleException(Exception ex) throws Exception {
    log.error("unexpected error " + ex.getClass(), ex);
    if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
      throw ex;
    Map<String, String> message = new HashMap();
    message.put("error", ex.getMessage());
    return message;
  }

}
