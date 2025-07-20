package domain.services;

import domain.fuentesDinamicas.FuenteDinamica;
import domain.fuentesDinamicas.HechoXFuente;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeFuentes;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeHechosXFuente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HechoService {
    private final RepositorioDeHechos repositorioDeHechos;
    private final RepositorioDeHechosXFuente repositorioDeHechosXFuente;
    private final RepositorioDeFuentes repositorioDeFuentes;
    
    public HechoService(RepositorioDeHechos repositorioDeHechos, RepositorioDeHechosXFuente repositorioDeHechosXFuente, RepositorioDeFuentes repositorioDeFuentes) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.repositorioDeHechosXFuente = repositorioDeHechosXFuente;
        this.repositorioDeFuentes = repositorioDeFuentes;
    }

    @Transactional(readOnly = true) // Asegura que la sesión esté abierta cuando se haga la serialización
    public List<Hecho> obtenerHechos() {
        return repositorioDeHechos.findAll();
    }

    @Transactional(readOnly = true)
    public List<Hecho> obtenerHechosConFechaMayorA(LocalDateTime fechaMayorA) {
        return repositorioDeHechos.findAll().stream().filter(hecho -> hecho.getFechaUltimaModificacion().isAfter(fechaMayorA)).toList();
    }

    public void guardarHechoEnFuente(Long id, Hecho hecho) {
        FuenteDinamica fuente = repositorioDeFuentes.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fuente no encontrada con ID: " + id));
        HechoXFuente hechoXFuente = new HechoXFuente(hecho, fuente);
        repositorioDeHechos.save(hecho);
        repositorioDeHechosXFuente.save(hechoXFuente);
    }

    @Transactional(readOnly = true)
    public List<Hecho> obtenerHechosDeFuente(Long id) {
        FuenteDinamica fuente = repositorioDeFuentes.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fuente no encontrada con ID: " + id));
        List<HechoXFuente> hechosPorFuente = repositorioDeHechosXFuente.findByFuente(fuente);
        return hechosPorFuente.stream()
                .map(HechoXFuente::getHecho)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Hecho> obtenerHechosDeFuenteConFechaMayorA(Long id, LocalDateTime fechaMayorA) {
        return obtenerHechosDeFuente(id).stream().filter(hecho -> hecho.getFechaUltimaModificacion().isAfter(fechaMayorA))
                .toList();
    }

    public Hecho obtenerHecho(String id) {
        return repositorioDeHechos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hecho no encontrado con ID: " + id));
    }
}
