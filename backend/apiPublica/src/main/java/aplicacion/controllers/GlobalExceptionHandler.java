package aplicacion.controllers;

import aplicacion.exceptions.NoInstanceException;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.client5.http.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.net.http.HttpTimeoutException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpHostConnectException.class)
    public ResponseEntity<?> handleWebClientException(HttpHostConnectException ex) {
        warnFalloDeConexion(ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }


    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<?> handleWebClientException(ResourceAccessException ex) {
        warnFalloDeConexion(ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<?> handleFailedRequest(HttpResponseException ex) {
        return ResponseEntity.status(ex.getStatusCode()).build();
    }
    @ExceptionHandler(HttpTimeoutException.class)
    public ResponseEntity<?> handleTimeoutException(HttpTimeoutException ex) {
        warnFalloDeConexion(ex);
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
    }
    @ExceptionHandler(NoInstanceException.class)
    public ResponseEntity<?> handleNoInstances(NoInstanceException ex) {
        warnFalloDeConexion(ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
    private void warnFalloDeConexion(Exception ex){
        logger.warn("Fallo de conexion: {}", ex.getMessage());
    }
}