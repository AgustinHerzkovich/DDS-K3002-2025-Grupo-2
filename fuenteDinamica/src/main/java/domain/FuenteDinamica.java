package domain;

import domain.Hechos.Hecho;
import domain.Repositorios.Repositorio;

import java.util.List;

// FUENTE DINAMICA
public class FuenteDinamica implements Fuente {
    private Repositorio repositorio;

    public FuenteDinamica(Repositorio repositorio){
        this.repositorio = repositorio;
    }

    public void agregarHecho(Hecho hecho){
        repositorio.agregarHecho(hecho);
    }

    public void quitarHecho(Hecho hecho){
        repositorio.quitarHecho(hecho);
    }

    public Hecho buscarHecho(Hecho hecho){
        return repositorio.buscarHecho(hecho);
    }

    public List<Hecho> importarHechos() {
        return repositorio.listarHechos();
    }
}