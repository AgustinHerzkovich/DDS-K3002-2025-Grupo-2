package aplicacion.config;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.*;
import io.swagger.v3.oas.models.media.*;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;


import java.util.List;

@Configuration
public class SwaggerConfig {
    // Se puede usar para borrar todos los paths no manuales
    /*
    @Bean
    public GroupedOpenApi manualApi() {
        return GroupedOpenApi.builder()
                .group("manual")
                .packagesToScan("")
                .build();
    }*/
    @Bean
    public OpenAPI customOpenAPI() {

        Paths paths = new Paths();
        paths.addPathItem("/apiPublica/solicitudes", new PathItem()
                .post(new Operation()
                        .tags(List.of("solicitud-controller"))
                        .summary("Crear una nueva solicitud")
                        .description("Crea una solicitud con solicitanteId, hechoId y motivo")
                        .requestBody(new io.swagger.v3.oas.models.parameters.RequestBody()
                                .required(true)
                                .content(new Content()
                                        .addMediaType("application/json",
                                                new MediaType()
                                                        .schema(new ObjectSchema()
                                                                .addProperties("solicitanteId", new IntegerSchema().example(0))
                                                                .addProperties("hechoId", new StringSchema().example("string"))
                                                                .addProperties("motivo", new StringSchema().example("string"))
                                                        )
                                        )
                                )
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content()
                                                .addMediaType("application/json",
                                                        new MediaType()
                                                                .schema(new ObjectSchema()
                                                                        .addProperties("id", new IntegerSchema().example(0))
                                                                        .addProperties("solicitanteId", new IntegerSchema().example(0))
                                                                        .addProperties("hechoId", new StringSchema().example("string"))
                                                                        .addProperties("motivo", new StringSchema().example("string"))
                                                                        .addProperties("estado", new ObjectSchema()
                                                                                .addProperties("estado", new StringSchema().example("string"))
                                                                        )
                                                                        .addProperties("fechaSubida", new StringSchema().example("2025-11-28T23:10:19.848Z"))
                                                                )
                                                )
                                        )
                                )
                        )
                ));

        paths.addPathItem("/apiPublica/contribuyentes", new PathItem()
                .get(new Operation()
                        .operationId("obtenerContribuyentes")
                        .tags(List.of("contribuyente-controller"))
                        .summary("Obtener contribuyentes")
                        .description("Devuelve la lista de todos los contribuyentes")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ArraySchema() // <--- Cambiado a ArraySchema
                                                        .items(new ObjectSchema()
                                                                .addProperties("id", new IntegerSchema().example(1))
                                                                .addProperties("esAdministrador", new BooleanSchema().example(true))
                                                                .addProperties("identidad", new ObjectSchema()
                                                                        .addProperties("nombre", new StringSchema().example("Lucas Martin"))
                                                                        .addProperties("apellido", new StringSchema().example("Presotto"))
                                                                        .addProperties("fechaNacimiento", new StringSchema().format("date").example(null))
                                                                )
                                                                .addProperties("mail", new StringSchema().example("lpresotto@frba.utn.edu.ar"))
                                                        )
                                                )
                                        ))
                                )
                        )
                ).post(new Operation()
                        .tags(List.of("contribuyente-controller"))
                        .summary("Crear contribuyente")
                        .description("Crea un nuevo contribuyente")
                        .requestBody(new RequestBody()
                                .required(true)
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new ObjectSchema()
                                                .addProperties("nombre", new StringSchema().example("Juan Pérez"))
                                                .addProperties("documento", new StringSchema().example("12345678"))
                                        )
                                ))
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Contribuyente creado")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ObjectSchema()
                                                        .addProperties("id", new IntegerSchema().example(1))
                                                        .addProperties("nombre", new StringSchema().example("Juan Pérez"))
                                                        .addProperties("documento", new StringSchema().example("12345678"))
                                                )
                                        ))
                                )
                        )
                )
        );

        paths.addPathItem("/apiPublica/contribuyentes/{id}/identidad", new PathItem()
                .patch(new Operation()
                        .tags(List.of("contribuyente-controller"))
                        .summary("Modificar identidad de un contribuyente")
                        .description("Actualiza la información de identidad de un contribuyente existente")
                        .addParametersItem(new Parameter()
                                .name("id")
                                .in("path")
                                .required(true)
                                .schema(new IntegerSchema())
                                .description("ID del contribuyente")
                        )
                        .requestBody(new RequestBody()
                                .required(true)
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new ObjectSchema()
                                                .addProperties("nombre", new StringSchema().example("Juan Pérez"))
                                                .addProperties("documento", new StringSchema().example("12345678"))
                                        )
                                ))
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse().description("Identidad actualizada"))
                        )
                )
        );

        paths.addPathItem("/apiPublica/contribuyentes/{id}/hechos", new PathItem()
                .get(new Operation()
                        .tags(List.of("contribuyente-controller"))
                        .summary("Obtener hechos de un contribuyente")
                        .description("Devuelve la lista de hechos asociados a un contribuyente")
                        .addParametersItem(new Parameter()
                                .name("id")
                                .in("path")
                                .required(true)
                                .schema(new IntegerSchema())
                                .description("ID del contribuyente")
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ObjectSchema()
                                                        .addProperties("hechoId", new IntegerSchema().example(1))
                                                        .addProperties("descripcion", new StringSchema().example("Hecho de prueba"))
                                                )
                                        ))
                                )
                        )
                )
        );

        paths.addPathItem("/apiPublica/hechos", new PathItem()
                .get(new Operation()
                        .tags(List.of("hecho-controller"))
                        .summary("Obtener todos los hechos")
                        .description("Devuelve la lista completa de hechos, usando paginacion para limitar el tamaño de las respuestas y utilizando los filtros especificados.")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ObjectSchema()
                                                        .addProperties("id", new IntegerSchema().example(1))
                                                        .addProperties("descripcion", new StringSchema().example("Hecho de prueba"))
                                                )
                                        ))
                                )
                        )
                )
        );

        paths.addPathItem("/apiPublica/hechos/{id}", new PathItem()
                .get(new Operation()
                        .tags(List.of("hecho-controller"))
                        .summary("Obtener hecho por ID")
                        .description("Devuelve un hecho específico según su ID")
                        .addParametersItem(new Parameter()
                                .name("id")
                                .in("path")
                                .required(true)
                                .schema(new IntegerSchema())
                                .description("ID del hecho")
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ObjectSchema()
                                                        .addProperties("id", new IntegerSchema().example(1))
                                                        .addProperties("descripcion", new StringSchema().example("Hecho de prueba"))
                                                )
                                        ))
                                )
                        )
                )
        );

        paths.addPathItem("/apiPublica/hechos/index", new PathItem()
                .get(new Operation()
                        .tags(List.of("hecho-controller"))
                        .summary("Autocompletar hechos / index")
                        .description("Devuelve datos para autocompletar las recomendaciones de busqueda de hechos")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ObjectSchema()
                                                        .addProperties("id", new IntegerSchema().example(1))
                                                        .addProperties("descripcion", new StringSchema().example("Hecho de prueba"))
                                                )
                                        ))
                                )
                        )
                )
        );

        paths.addPathItem("/apiPublica/colecciones", new PathItem()
                .get(new Operation()
                        .tags(List.of("coleccion-controller"))
                        .summary("Mostrar todas las colecciones")
                        .description("Devuelve la lista completa de colecciones")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ArraySchema()
                                                        .items(new ObjectSchema()
                                                                .addProperties("id", new IntegerSchema().example(1))
                                                                .addProperties("nombre", new StringSchema().example("Colección de prueba"))
                                                        )
                                                )
                                        ))
                                )
                        )
                )
        );

        paths.addPathItem("/apiPublica/colecciones/{id}", new PathItem()
                .get(new Operation()
                        .tags(List.of("coleccion-controller"))
                        .summary("Mostrar colección por ID")
                        .description("Devuelve una colección específica según su ID")
                        .addParametersItem(new Parameter()
                                .name("id")
                                .in("path")
                                .required(true)
                                .schema(new IntegerSchema())
                                .description("ID de la colección")
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ObjectSchema()
                                                        .addProperties("id", new IntegerSchema().example(1))
                                                        .addProperties("nombre", new StringSchema().example("Colección de prueba"))
                                                )
                                        ))
                                )
                        )
                )
        );

        paths.addPathItem("/apiPublica/colecciones/{id}/hechosIrrestrictos", new PathItem()
                .get(new Operation()
                        .tags(List.of("coleccion-controller"))
                        .summary("Mostrar hechos irrestrictos de la colección")
                        .description("Devuelve los hechos irrestrictos asociados a una colección")
                        .addParametersItem(new Parameter()
                                .name("id")
                                .in("path")
                                .required(true)
                                .schema(new IntegerSchema())
                                .description("ID de la colección")
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ArraySchema()
                                                        .items(new ObjectSchema()
                                                                .addProperties("id", new IntegerSchema().example(101))
                                                                .addProperties("descripcion", new StringSchema().example("Hecho irrestricto"))
                                                        )
                                                )
                                        ))
                                )
                        )
                )
        );

        paths.addPathItem("/apiPublica/colecciones/{id}/hechosCurados", new PathItem()
                .get(new Operation()
                        .tags(List.of("coleccion-controller"))
                        .summary("Mostrar hechos curados de la colección")
                        .description("Devuelve los hechos curados asociados a una colección")
                        .addParametersItem(new Parameter()
                                .name("id")
                                .in("path")
                                .required(true)
                                .schema(new IntegerSchema())
                                .description("ID de la colección")
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ArraySchema()
                                                        .items(new ObjectSchema()
                                                                .addProperties("id", new IntegerSchema().example(201))
                                                                .addProperties("descripcion", new StringSchema().example("Hecho curado"))
                                                        )
                                                )
                                        ))
                                )
                        )
                )
        );

        paths.addPathItem("/apiPublica/colecciones/index", new PathItem()
                .get(new Operation()
                        .tags(List.of("coleccion-controller"))
                        .summary("Autocompletar colecciones / índice")
                        .description("Devuelve datos para autocompletar en las busquedas de coleccoines")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new ArraySchema()
                                                        .items(new ObjectSchema()
                                                                .addProperties("id", new IntegerSchema().example(1))
                                                                .addProperties("nombre", new StringSchema().example("Colección de prueba"))
                                                        )
                                                )
                                        ))
                                )
                        )
                )
        );











        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API Pública de Metamapa")
                        .version("1.0")
                        .description("● Consulta de hechos dentro de una colección.\n" +
                                "● Generar una solicitud de eliminación a un hecho.\n" +
                                "● Navegación filtrada sobre una colección.\n" +
                                "● Navegación curada o irrestricta sobre una colección.\n" +
                                "● Reportar un hecho.")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("DDS K3002 Grupo 2")
                                .email("aherzkovich@frba.utn.edu.ar"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .servers(List.of(
                        new io.swagger.v3.oas.models.servers.Server()
                                .url("http://localhost:8085")
                )).paths(paths);
    }
}