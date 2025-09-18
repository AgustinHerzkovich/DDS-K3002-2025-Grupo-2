package aplicacion.controllers;

import aplicacion.domain.dimensiones.DimensionCategoria;
import aplicacion.repositorios.DimensionCategoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {
    DimensionCategoriaRepository categoriaRepository;

    public EstadisticasController(DimensionCategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }
    @PostConstruct
    public void algo(){
        categoriaRepository.save(new DimensionCategoria(2L, "Cate"));
    }
}
