package domain.services;

import domain.fuentesEstaticas.Fuente;
import domain.fuentesEstaticas.FuenteEstatica;
import domain.fuentesEstaticas.LectorCsv;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeFuentes;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class FuenteService {
    private final RepositorioDeFuentes repositorioDeFuentes;
    private final LectorCsv lectorCsv;

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes) {
        this.repositorioDeFuentes = repositorioDeFuentes;
        this.lectorCsv = new LectorCsv();
    }

    public List<Hecho> obtenerTodosLosHechos() {
        List<Hecho> hechos = new ArrayList<>();
        repositorioDeFuentes.findAll().forEach(fuente ->
                hechos.addAll(fuente.importarHechos())
        );
        return hechos;

    }

    public List<Hecho> obtenerHechosPorFuente(Long id) {
        Fuente fuente = repositorioDeFuentes.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la fuente con id " + id));
        return fuente.importarHechos();
    }

    public FuenteEstatica crearFuenteEstatica(List<String> archivos) {
        FuenteEstatica nuevaFuente = new FuenteEstatica(lectorCsv, null); // El ID lo generará JPA
        archivos.forEach(nuevaFuente::agregarArchivo);
        return repositorioDeFuentes.save(nuevaFuente);
    }
}
