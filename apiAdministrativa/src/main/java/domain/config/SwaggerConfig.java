package domain.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Administrativa de Metamapa",
                version = "1.0",
                description = "● Operaciones CRUD sobre las colecciones.\n" +
                        "● Modificación del algoritmo de consenso.\n" +
                        "● Agregar o quitar fuentes de hechos de una colección.\n" +
                        "● Aprobar o denegar una solicitud de eliminación de un hecho.",
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