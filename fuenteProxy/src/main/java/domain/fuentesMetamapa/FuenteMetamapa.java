package domain.fuentesMetamapa;

import domain.criterios.CriterioDePertenencia;
import domain.hechos.Hecho;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// METAMAPA EXTERNO
public class FuenteMetamapa extends FuenteProxy {
    public static List<Hecho> obtenerHechos() {
        String url = "https://mocki.io/v1/f7baa69f-a201-4766-b1e8-2618ebc381fb";
        List<Hecho> hechosObtenidos = List.of(get(url, Hecho[].class)); //
        return hechosObtenidos;
    }

    public List<Hecho> obtenerHechosColeccion(String identificador) {
        String url = "https://mocki.io/v1/536a4049-29e5-4cb2-89c7-76c1b068a1a1";
        List<Hecho> hechosObtenidos = List.of(get(url, Hecho[].class)); // Usa el metodo heredado de FuenteProxy
        return hechosObtenidos;
    }
/*
    public void postearSolicitud(String json) {
        //TODO
    }

    public Hecho formatearHecho(String json){
        //TODO
    }

    public List<Hecho> filtrarHechos(List<Hecho> hechos,List<CriterioDePertenencia> filtros) {
        //TODO
    }

    public List<Hecho> importarHechos() {
        // TODO
    }*/
}