package aplicacion.config;

import org.springframework.ui.Model;

public class TokenContext {
    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }
    public static String getToken() {
        return tokenThreadLocal.get();
    }
    public static void clear() {
        tokenThreadLocal.remove();
    }

    public static void addToken(Model model){
        String token = getToken();
        if(token != null){
            model.addAttribute("JWT_Token", token);
        }
    }
}