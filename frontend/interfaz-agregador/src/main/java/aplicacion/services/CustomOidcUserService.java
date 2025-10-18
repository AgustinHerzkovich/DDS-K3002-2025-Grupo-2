package aplicacion.services;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

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

        // 2. Ejecutamos nuestra lógica personalizada de registro "Just-In-Time"
        usuarioService.registrarUsuarioSiNoExiste(oidcUser);

        // 3. Devolvemos el usuario para que la autenticación continúe
        return oidcUser;
    }
}
