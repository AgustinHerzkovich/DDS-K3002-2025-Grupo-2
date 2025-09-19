package aplicacion.controllers;

import aplicacion.repositorios.agregador.HechoRepository;
import aplicacion.repositorios.olap.DimensionCategoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import aplicacion.domain.hechosYSolicitudes.Hecho;
import java.util.Optional;
@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {

    HechoRepository hechoRepository;
    public EstadisticasController(HechoRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }

    @PostConstruct
    public void testear( ){
        System.out.println("EstadisticasController: testear() ");
        Optional<Hecho> ej = hechoRepository.findById("0009ec55-2721-4827-a225-df3f7bcc50d9");
        System.out.println(ej.isPresent() ? ej.get() : "No encontrado");
    }
}
