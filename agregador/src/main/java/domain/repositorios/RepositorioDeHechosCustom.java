package domain.repositorios;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;

import java.util.List;

public interface RepositorioDeHechosCustom {
    void saveByFuente(List<Hecho> hechos, Fuente fuente);

    List<Hecho> findByColeccionId(String idColeccion);
}
