package aplicacion.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Fuentes Dinámicas API",
                version = "1.0",
                description = "API para administrar fuentes dinámicas",
                contact = @Contact(
                        name = "DDS K3002 Grupo 2",
                        email = "aherzkovich@frba.utn.edu.ar"
                ),
                license = @License(
                        name = "MIT",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
public class SwaggerConfig {
}