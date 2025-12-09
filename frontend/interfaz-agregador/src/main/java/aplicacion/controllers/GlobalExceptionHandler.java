package aplicacion.controllers;

import aplicacion.config.TokenContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientRequestException.class)
    public String handleWebClientRequestException(WebClientRequestException ex, Model model) {
        TokenContext.addToken(model);

        return "error/502";
    }

    @ExceptionHandler(WebClientResponseException.class)
    public String handleWebClientResponseException(WebClientResponseException ex, HttpServletRequest request, HttpServletResponse response, Model model) {
        TokenContext.addToken(model);
        return "error/" + ex.getStatusCode().value();
    }
}