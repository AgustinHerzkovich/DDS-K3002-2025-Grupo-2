package domain.services;

import domain.repositorios.RepositorioDeFuentes;
import domain.colecciones.fuentes.Fuente;

import java.util.List;

public class FuenteService {
    private final RepositorioDeFuentes repositorio_de_fuentes;

    public FuenteService(RepositorioDeFuentes repositorio_de_fuentes) {
        this.repositorio_de_fuentes = repositorio_de_fuentes;
    }

    public void guardarFuentes(List<Fuente> fuentes) {
        repositorio_de_fuentes.saveOnlyNew(fuentes);
    }
}