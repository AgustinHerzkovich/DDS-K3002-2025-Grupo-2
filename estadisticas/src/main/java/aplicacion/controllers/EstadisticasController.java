package aplicacion.controllers;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.repositorios.DimensionCategoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {

    public EstadisticasController(DimensionCategoriaRepository categoriaRepository){
    }
}
