package aplicacion.controllers;

import aplicacion.repositorios.agregador.HechoRepository;
import aplicacion.repositorios.olap.DimensionCategoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import aplicacion.domain.hechosYSolicitudes.Hecho;

import java.util.List;
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
        Long hechos = hechoRepository.count();
        System.out.println( hechos + " Hechos");
        PageRequest pageRequest = PageRequest.of(0, 10);
        hechoRepository.findAll(pageRequest).getContent().forEach(System.out::println);
    }
}
