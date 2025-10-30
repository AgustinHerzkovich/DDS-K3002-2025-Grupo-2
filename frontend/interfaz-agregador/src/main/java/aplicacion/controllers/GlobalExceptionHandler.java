package aplicacion.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientException.class)
    public String handleWebClientException(WebClientException ex) {
        return "error/error-server-offline";
    }
}