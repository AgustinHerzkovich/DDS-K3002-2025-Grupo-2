package domain.services;

import domain.fuentesDinamicas.FuenteDinamica;
import domain.fuentesDinamicas.HechoXFuente;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeFuentes;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeHechosXFuente;
import org.springframework.stereotype.Service;

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

    public List<Hecho> obtenerHechos() {
        return repositorioDeHechos.findAll();
    }

    public void guardarHechoEnFuente(Long id, Hecho hecho) {
        FuenteDinamica fuente = repositorioDeFuentes.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fuente no encontrada con ID: " + id));
        HechoXFuente hechoXFuente = new HechoXFuente(hecho, fuente);
        repositorioDeHechos.save(hecho);
        repositorioDeHechosXFuente.save(hechoXFuente);
    }

    public List<Hecho> obtenerHechosDeFuente(Long id) {
        FuenteDinamica fuente = repositorioDeFuentes.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fuente no encontrada con ID: " + id));
        return repositorioDeHechosXFuente.findByFuente(fuente).stream()
                .map(HechoXFuente::getHecho)
                .toList();
    }

    public List<Hecho> obtenerHechosDeFuenteConFechaMayorA(Long id, LocalDateTime fechaMayorA) {
        return obtenerHechosDeFuente(id).stream().filter(hecho -> hecho.seActualizoDespuesDe(fechaMayorA))
                .toList();
    }

    public Hecho obtenerHecho(String id) {
        return repositorioDeHechos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hecho no encontrado con ID: " + id));
    }
}
