package domain.services;

import domain.repositorios.RepositorioDeFuentes;
import domain.colecciones.fuentes.Fuente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuenteService {
    private final RepositorioDeFuentes repositorioDeFuentes;

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes) {
        this.repositorioDeFuentes = repositorioDeFuentes;
    }

    public void guardarFuentes(List<Fuente> fuentes) {
        repositorioDeFuentes.saveOnlyNew(fuentes);
    }
}