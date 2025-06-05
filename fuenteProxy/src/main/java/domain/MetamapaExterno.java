package domain;
import domain.Criterios.CriterioDePertenencia;
import domain.Hechos.Hecho;

import java.util.List;

// METAMAPA EXTERNO
public class MetamapaExterno extends FuenteProxy {
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