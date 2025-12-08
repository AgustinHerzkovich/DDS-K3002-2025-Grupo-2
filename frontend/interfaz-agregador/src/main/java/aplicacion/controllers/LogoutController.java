package aplicacion.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logoutPage(CsrfToken token, Model model) {
        model.addAttribute("_csrf", token);
        return "logout";
    }
}
