package aplicacion.controllers;

import aplicacion.services.ColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ColeccionGraphQLController {
    @Autowired
    private ColeccionService coleccionService;

    /**
     * Consulta: coleccionPorId(id: ID!)
     * Debe coincidir con el nombre en schema.graphqls
     */
    @QueryMapping
    public Object coleccionPorId(@Argument String id) {
        // TODO: PONER LA LÓGICA REAL
        // return coleccionService.buscarPorId(id);

        // MOCK TEMPORAL (Para que compile)
        return null;
    }

    /**
     * Consulta: todasLasColecciones
     */
    @QueryMapping
    public List<Object> todasLasColecciones() {
        // TODO: PONER LA LÓGICA REAL
        // return coleccionService.obtenerTodas();
        return List.of(); // Retorna lista vacía por ahora
    }
}
