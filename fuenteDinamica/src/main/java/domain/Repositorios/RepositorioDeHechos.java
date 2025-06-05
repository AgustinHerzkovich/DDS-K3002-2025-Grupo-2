package domain.Repositorios;

import domain.Hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

// REPOSITORIO DE HECHOS
public class RepositorioDeHechos implements Repositorio {
    private List<Hecho> hechos;

    public RepositorioDeHechos() {
        this.hechos = new ArrayList<Hecho>();
    }

    public void agregarHecho(Hecho hecho){
        // TODO
    }

    public void quitarHecho(Hecho hecho) {
        // TODO
    }

    public Hecho buscarHecho(Hecho hecho) {
       // TODO
    }

    public List<Hecho> listarHechos(){
        // TODO
    }
}
