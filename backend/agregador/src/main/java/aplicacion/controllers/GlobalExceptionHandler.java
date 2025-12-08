package aplicacion.controllers;

import aplicacion.excepciones.*;
import aplicacion.services.FuenteMutexManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public ResponseEntity<?> error(HttpStatus status, String detail){
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
    }
    public ResponseEntity<?> error(HttpStatus status, Exception exception){
        return error(status, exception.getMessage());
    }
    public ResponseEntity<?> notFound(String detail){
        return error(HttpStatus.NOT_FOUND, detail);
    }
    public ResponseEntity<?> notFound(Exception e){
        return notFound(e.getMessage());
    }
    public ResponseEntity<?> badRequest(String detail){
        return error(HttpStatus.BAD_REQUEST, detail);
    }
    public ResponseEntity<?> badRequest(Exception e){
        return badRequest(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {
        logger.error("Error inesperado en peticion REST", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno en el servidor");
    }
    @ExceptionHandler(HechoNoEncontradoException.class)
    public ResponseEntity<?> handleHechoNotFound(HechoNoEncontradoException ex) {
        return notFound(ex);
    }
    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<?> handleCategoryNotFound(CategoriaNoEncontradaException ex) {
        return notFound(ex);
    }
    @ExceptionHandler(ColeccionNoEncontradaException.class)
    public ResponseEntity<?> handleColeccionNoEncontradaException(ColeccionNoEncontradaException ex) {
        return notFound(ex);
    }
    @ExceptionHandler(ContribuyenteNoConfiguradoException.class)
    public ResponseEntity<?> handleContribuyenteNoConfiguradoException(ContribuyenteNoConfiguradoException ex) {
        return notFound(ex);
    }
    @ExceptionHandler(EtiquetaNoEncontradaException.class)
    public ResponseEntity<?> handleEtiquetaNoEncontradaException(EtiquetaNoEncontradaException ex) {
        return notFound(ex);
    }
    @ExceptionHandler(FuenteNoEncontradaException.class)
    public ResponseEntity<?> handleFuenteNoEncontradaException(FuenteNoEncontradaException ex) {
        return notFound(ex);
    }
    @ExceptionHandler(MailYaExisteException.class)
    public ResponseEntity<?> handleMailYaExisteException(MailYaExisteException ex) {
        return badRequest(ex);
    }
    @ExceptionHandler(MotivoSolicitudException.class)
    public ResponseEntity<?> handleMotivoSolicitudException(MotivoSolicitudException ex) {
        return badRequest(ex);
    }
    @ExceptionHandler(UbicacionNoEncontradaException.class)
    public ResponseEntity<?> handleUbicacionNotFoundException(UbicacionNoEncontradaException ex) {
        return notFound(ex);
    }
    @ExceptionHandler(CategoriaYaPresenteException.class)
    public ResponseEntity<?> handleUbicacionNotFoundException(CategoriaYaPresenteException ex) {
        return badRequest(ex);
    }
    @ExceptionHandler(TipoDeFuenteErroneoException.class)
    public ResponseEntity<?> handleTipoDeFuenteErroneaException(TipoDeFuenteErroneoException ex) {
        return badRequest(ex);
    }
    @ExceptionHandler(InvalidPageException.class)
    public ResponseEntity<?> handleInvalidPages(InvalidPageException ex) {
        return badRequest(ex);
    }
    @ExceptionHandler(TooHighLimitException.class)
    public ResponseEntity<?> handleTooHighLimit(TooHighLimitException ex) {
        return badRequest(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }
}