package aplicacion.controllers;

import org.apache.hc.client5.http.HttpHostConnectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpHostConnectException.class)
    public ResponseEntity<?> handleWebClientException(HttpHostConnectException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
}