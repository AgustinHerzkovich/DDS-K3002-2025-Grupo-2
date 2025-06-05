package domain.Repositorios;

import domain.Hechos.Hecho;

import java.util.List;

public interface Repositorio {
    void agregarHecho(Hecho hecho);

    void quitarHecho(Hecho hecho);

    Hecho buscarHecho(Hecho hecho);

    List<Hecho> listarHechos();

}
