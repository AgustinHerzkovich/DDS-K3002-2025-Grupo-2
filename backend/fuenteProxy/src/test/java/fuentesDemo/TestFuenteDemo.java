package fuentesDemo;

import aplicacion.domain.fuentesProxy.fuentesDemo.Conexion;
import aplicacion.domain.fuentesProxy.fuentesDemo.ConexionPrueba;
import aplicacion.domain.fuentesProxy.fuentesDemo.FuenteDemo;
import aplicacion.domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
import aplicacion.domain.hechos.Hecho;

public class TestFuenteDemo {
    // Crear una fuente demo con una biblioteca de prueba que devuelva 2 hechos
    public static void main(String[] args) {
        Conexion conexionPrueba = new ConexionPrueba();
        FuenteDemo fuente = new FuenteDemo(conexionPrueba, "http://fake-url.com");

        fuente.pedirHechos();

        /*System.out.println("Hechos importados:");
        for (Hecho h : fuente.importarHechos()) {
            System.out.println(h.getTitulo() + " - " + h.getDescripcion() + " - " + h.getFechaAcontecimiento());
        }

        FuenteMetamapa fuenteMetamapa= new FuenteMetamapa("http://localhost:8084/agregador/hechos");
        System.out.println("Hechos importados:");
        for (Hecho h : fuenteMetamapa.importarHechos()) {
            System.out.println(h.getTitulo() + " - "+ h.getOrigen() +" - " + h.getDescripcion() + " - " + h.getFechaAcontecimiento());
        }*/
    }
}

/*{
        "titulo": "Coleccion 1",
        "descripcion": "Coleccion de colecciones jijo",
        "criteriosDePertenencia": [
        {
        "tipo": "fecha",
        "fechaInicial": "2010-01-01T20:44:13.170",
        "fechaFinal": "2020-12-20T20:44:13.170"
        }
        ],
        "fuentes": [
        {
        "id": {
        "tipo": "ESTATICA",
        "idExterno": 1
        }
        },
        {
        "id": {
        "tipo": "DINAMICA",
        "idExterno": 1
        }
        }
        ],
        "algoritmoConsenso": {
        "tipo": "absoluto"
        }
}
*/
