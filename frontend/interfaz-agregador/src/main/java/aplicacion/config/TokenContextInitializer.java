package aplicacion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TokenContextInitializer {

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @PostConstruct
    public void init() {
        TokenContext.setClientService(clientService);
    }
}

