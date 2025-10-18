package aplicacion.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        String targetUrl = (String) request.getSession().getAttribute("REDIRECT_URL_AFTER_LOGIN");

        if (targetUrl == null || targetUrl.isEmpty()) {
            targetUrl = "/";
        }

        request.getSession().removeAttribute("REDIRECT_URL_AFTER_LOGIN");
        response.sendRedirect(targetUrl);
    }
}