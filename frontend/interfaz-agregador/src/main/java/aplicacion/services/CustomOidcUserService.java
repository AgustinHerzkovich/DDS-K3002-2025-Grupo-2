package aplicacion.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UsuarioService usuarioService;

    public CustomOidcUserService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Obtenemos el usuario de Keycloak como lo haría Spring por defecto
        OidcUser oidcUser = super.loadUser(userRequest);

        // 2. Extraer roles del claim "realm_roles" y convertirlos en authorities
        Set<GrantedAuthority> authorities = new HashSet<>(extractAuthorities(oidcUser));

        // 3. Crear un nuevo OidcUser con las authorities correctas
        OidcUser enrichedUser = new DefaultOidcUser(
                authorities,
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "sub" // nombre del claim que identifica al usuario
        );

        // 4. Ejecutamos nuestra lógica personalizada de registro "Just-In-Time"
        usuarioService.registrarUsuarioSiNoExiste(enrichedUser);

        // 5. Devolvemos el usuario enriquecido con los roles
        return enrichedUser;
    }

    /**
     * Extrae los roles del claim "realm_roles" de Keycloak y los convierte en authorities
     */
    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractAuthorities(OidcUser oidcUser) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Extraer roles del claim "realm_roles" (donde Keycloak pone los roles)
        Object rolesClaim = oidcUser.getClaim("realm_roles");

        if (rolesClaim instanceof Collection<?>) {
            Collection<String> roles = ((Collection<?>) rolesClaim).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());

            // Convertir cada rol a una authority con prefijo ROLE_
            Set<GrantedAuthority> newAuthorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toSet());

            authorities.addAll(newAuthorities);
        }

        // Mantener las authorities originales también
        authorities.addAll(oidcUser.getAuthorities());

        return authorities;
    }
}
