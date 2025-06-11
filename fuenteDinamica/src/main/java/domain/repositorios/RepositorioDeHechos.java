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
        hechos.add(hecho);
    }

    public void quitar(Hecho hecho) {
        hechos.remove(hecho);
    }

    public Hecho buscar(Hecho hecho) {
       return hechos.stream().filter(h -> h.equals(hecho)).findFirst().orElse(null);
    }

    public List<Hecho> listar(){
        return hechos;
    }
}
