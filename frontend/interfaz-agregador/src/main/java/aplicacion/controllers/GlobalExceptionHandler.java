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
        if (ex.getStatusCode().value() == 401) {
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();

            request.getSession().setAttribute("REDIRECT_URL_AFTER_LOGIN", request.getRequestURI());
            String loginUrl = "/oauth2/authorization/keycloak";
            try {
                response.sendRedirect(loginUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        return "error/" + ex.getStatusCode().value();
    }
}