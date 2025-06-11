package domain;

import domain.fuentesEstaticas.FuenteEstatica;
import domain.fuentesEstaticas.LectorCsv;
import domain.hechos.Hecho;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import java.io.IOException;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController // Le decimos que esta clase es un controlador REST
// Esto significa que es lo que *expone la api* para que sea consumido
@RequestMapping("/fuentesEstaticas") // Define ruta base para todos los endpoints de esta clase
public class FuenteEstaticaController {
    private final Map<Long, FuenteEstatica> fuentes = new HashMap<>(); // Registro simulado (en memoria)

    public FuenteEstaticaController() throws IOException {
        LectorCsv lector_mock = new LectorCsv();
        FuenteEstatica fuente = new FuenteEstatica(lector_mock, 1L);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] recursos = resolver.getResources("classpath:/ArchivosCsvPrueba/*.csv");

        for (Resource recurso : recursos) {
            fuente.agregarArchivo(recurso.getFile().getPath());
        }

        fuentes.put(fuente.getId(), fuente);
    }

    @GetMapping("/hechos/{id}") //Se ejecuta al hacer GET en este id
    public List<Hecho> hechos(@PathVariable("id") Long id) {
        FuenteEstatica fuente = fuentes.get(id);
        if (fuente == null){
            throw new NoSuchElementException("No se encontro la fuente con id " + id);// TODO: mas adelante implementar exceptions personalizados
        }
        return fuente.importarHechos();
    }
}


