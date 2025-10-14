package aplicacion.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AuthController {
    
    /**
     * Manejador personalizado para el cierre de sesión que permite redirigir a la página 
     * desde la que se cerró sesión después de completar el proceso.
     */
    @PostMapping("/logout-custom")
    public RedirectView logoutCustom(
            @RequestParam(value = "redirectUrl", defaultValue = "/") String redirectUrl,
            HttpServletRequest request, 
            HttpServletResponse response) {
        
        // Obtener la autenticación actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Si hay una autenticación activa, realizar el logout
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        
        // Redirigir a la URL especificada (o a la página de inicio por defecto)
        return new RedirectView(redirectUrl);
    }
}
