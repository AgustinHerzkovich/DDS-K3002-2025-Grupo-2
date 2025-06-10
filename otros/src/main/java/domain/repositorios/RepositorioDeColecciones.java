package domain.repositorios;

import domain.colecciones.Coleccion;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeColecciones implements Repositorio {
    // REPOSITORIO DE COLECCIONES

        private List<Coleccion> colecciones;

        public RepositorioDeColecciones() {
            this.colecciones = new ArrayList<Coleccion>();
        }

        public void agregar(Coleccion Coleccion){
            // TODO
        }

        public void quitar(Coleccion Coleccion) {
            // TODO
        }

        public Coleccion buscar(Coleccion Coleccion) {
            // TODO
        }

        public List<Coleccion> listar(){
            // TODO
        }
    }
}
