package domain.fuentesMetamapa;

import domain.criterios.CriterioDePertenencia;
import domain.hechos.Hecho;

import java.util.List;

// METAMAPA EXTERNO
public class FuenteMetamapa extends FuenteProxy {
    public List<String> obtenerHechos(){
        //TODO
    }

    public Hecho formatearHecho(String json){
        //TODO
    }

    public List<Hecho> filtrarHechos(List<Hecho> hechos,List<CriterioDePertenencia> filtros) {
        //TODO
    }

    public List<Hecho> obtenerHechosColeccion(String identificador) {
        //TODO
    }

    public void postearSolicitud(String json) {
        //TODO
    }

    public List<Hecho> importarHechos() {
        // TODO
    }
}