package aplicacion.controllers;

import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.client5.http.HttpResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.net.http.HttpTimeoutException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpHostConnectException.class)
    public ResponseEntity<?> handleWebClientException(HttpHostConnectException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }


    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<?> handleWebClientException(ResourceAccessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<?> handleFailedRequest(HttpResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode()).build();
    }
    @ExceptionHandler(HttpTimeoutException.class)
    public ResponseEntity<?> handleTimeoutException(HttpTimeoutException ex) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
    }
}