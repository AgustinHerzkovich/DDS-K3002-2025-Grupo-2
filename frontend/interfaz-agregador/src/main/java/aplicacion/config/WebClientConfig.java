package aplicacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

// Configuración de WebClient para comunicarse con la API Administrativa
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient apiAdministrativaWebClient(
            OAuth2AuthorizedClientService clientService) {
        return WebClient.builder()
                .baseUrl("http://api-administrativa:8086/apiAdministrativa")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024) // 16MB
                        )
                        .build())
                .filter((request, next) -> {

                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                                oauthToken.getAuthorizedClientRegistrationId(),
                                oauthToken.getName()
                        );

                        if (client != null && client.getAccessToken() != null) {
                            TokenContext.setToken(client.getAccessToken().getTokenValue());
                            return next.exchange(
                                    ClientRequest.from(request)
                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue())
                                            .build()
                            );
                        }
                    }

                    return next.exchange(request);
                })
                .build();
    }
}
