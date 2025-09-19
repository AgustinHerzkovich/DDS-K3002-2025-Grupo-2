package aplicacion.controllers;

import aplicacion.repositorios.olap.DimensionCategoriaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {

    public EstadisticasController(DimensionCategoriaRepository categoriaRepository){
    }
}
