package domain.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Utilidad para decodificar tokens JWT sin necesidad de validar la firma.
 * Se usa para extraer información del usuario del token.
 */
public class JwtUtil {

    /**
     * Extrae el ID del usuario (subject) del token JWT.
     *
     * @param token Token JWT (puede incluir o no el prefijo "Bearer ")
     * @return ID del usuario extraído del token, o null si el token es inválido
     */
    public static String extractUserId(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            // Remover el prefijo "Bearer " si existe
            String cleanToken = token.startsWith("Bearer ")
                ? token.substring(7)
                : token;

            // Decodificar el token sin validar la firma
            // (la validación ya se hizo en el gateway/keycloak)
            DecodedJWT decodedJWT = JWT.decode(cleanToken);

            // El subject del JWT típicamente contiene el ID del usuario
            return decodedJWT.getSubject();
        } catch (Exception e) {
            System.err.println("Error al decodificar el token JWT: " + e.getMessage());
            return null;
        }
    }

    /**
     * Extrae un claim específico del token JWT.
     *
     * @param token Token JWT
     * @param claimName Nombre del claim a extraer
     * @return Valor del claim como String, o null si no existe
     */
    public static String extractClaim(String token, String claimName) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            String cleanToken = token.startsWith("Bearer ")
                ? token.substring(7)
                : token;

            DecodedJWT decodedJWT = JWT.decode(cleanToken);

            return decodedJWT.getClaim(claimName).asString();
        } catch (Exception e) {
            System.err.println("Error al extraer claim del token JWT: " + e.getMessage());
            return null;
        }
    }
}

