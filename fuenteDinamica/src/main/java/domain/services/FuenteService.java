package domain.services;

import domain.fuentesDinamicas.FuenteDinamica;
import domain.repositorios.RepositorioDeFuentes;
import org.springframework.stereotype.Service;

@Service
public class FuenteService {
    private final RepositorioDeFuentes repositorioDeFuentes;

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes) {
        this.repositorioDeFuentes = repositorioDeFuentes;
    }

    public FuenteDinamica guardarFuente() {
        FuenteDinamica fuente = new FuenteDinamica();
        return repositorioDeFuentes.save(fuente);
    }

    public void eliminarFuente(FuenteDinamica fuente) {
        if (repositorioDeFuentes.existsById(fuente.getId())) {
            repositorioDeFuentes.delete(fuente);
        } else {
            throw new IllegalArgumentException("Fuente no encontrada con ID: " + fuente.getId());
        }
    }
}
