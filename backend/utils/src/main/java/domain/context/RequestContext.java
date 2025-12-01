package domain.context;

public class RequestContext {
    private static final ThreadLocal<String> authToken = new ThreadLocal<>();

    public static void setAuthToken(String token) {
        authToken.set(token);
    }

    public static String getAuthToken() {
        return authToken.get();
    }

    public static void clear() {
        authToken.remove();
    }
}
