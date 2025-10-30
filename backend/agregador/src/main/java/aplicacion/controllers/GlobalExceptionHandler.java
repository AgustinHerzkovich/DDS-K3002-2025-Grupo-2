package aplicacion.controllers;

import aplicacion.excepciones.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {
        return ResponseEntity.internalServerError().build();
    }
    @ExceptionHandler(HechoNoEncontradoException.class)
    public ResponseEntity<?> handleHechoNotFound(HechoNoEncontradoException ex) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<?> handleCategoryNotFound(CategoriaNoEncontradaException ex) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(ColeccionNoEncontradaException.class)
    public ResponseEntity<?> handleColeccionNoEncontradaException(ColeccionNoEncontradaException ex) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(ContribuyenteNoConfiguradoException.class)
    public ResponseEntity<?> handleContribuyenteNoConfiguradoException(ContribuyenteNoConfiguradoException ex) {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(EtiquetaNoEncontradaException.class)
    public ResponseEntity<?> handleWebClientRequestException(EtiquetaNoEncontradaException ex) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(FuenteNoEncontradaException.class)
    public ResponseEntity<?> handleFuenteNoEncontradaException(FuenteNoEncontradaException ex) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(MailYaExisteException.class)
    public ResponseEntity<?> handleMailYaExisteException(MailYaExisteException ex) {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(MotivoSolicitudException.class)
    public ResponseEntity<?> handleMotivoSolicitudException(MotivoSolicitudException ex) {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(UbicacionNoEncontradaException.class)
    public ResponseEntity<?> handleUbicacionNotFoundException(UbicacionNoEncontradaException ex) {
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(CategoriaYaPresenteException.class)
    public ResponseEntity<?> handleUbicacionNotFoundException(CategoriaYaPresenteException ex) {
        return ResponseEntity.badRequest().build();
    }

}