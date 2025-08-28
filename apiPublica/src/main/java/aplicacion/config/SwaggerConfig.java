package aplicacion.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Pública para otras instancias de Metamapa",
                version = "1.0",
                description = "● Consulta de hechos dentro de una colección.\n" +
                        "● Generar una solicitud de eliminación a un hecho.\n" +
                        "● Navegación filtrada sobre una colección.\n" +
                        "● Navegación curada o irrestricta sobre una colección.\n" +
                        "● Reportar un hecho.",
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