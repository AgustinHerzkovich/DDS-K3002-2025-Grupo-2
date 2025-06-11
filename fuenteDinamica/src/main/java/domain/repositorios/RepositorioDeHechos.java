package domain.repositorios;

import domain.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

// REPOSITORIO DE HECHOS
public class RepositorioDeHechos implements Repositorio<Hecho> {
    private List<Hecho> hechos;

    public RepositorioDeHechos() {
        this.hechos = new ArrayList<Hecho>();
    }

    public void agregar(Hecho hecho){
        // TODO
    }

    public void quitar(Hecho hecho) {
        // TODO
    }

    public Hecho buscar(Hecho hecho) {
       // TOD0
        return null;
    }

    public List<Hecho> listar(){
        return null;
    }
}
