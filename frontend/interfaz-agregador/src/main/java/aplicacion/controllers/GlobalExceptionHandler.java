package aplicacion.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientRequestException.class)
    public String handleWebClientRequestException(WebClientRequestException ex) {
        return "error/502";
    }

    @ExceptionHandler(WebClientResponseException.class)
    public String handleWebClientResponseException(WebClientResponseException ex) {
        if(ex.getStatusCode() == HttpStatus.BAD_GATEWAY) // 502, no se pudo conectar a agregador
            return "error/502";
        else
            return "error/500";
    }
}