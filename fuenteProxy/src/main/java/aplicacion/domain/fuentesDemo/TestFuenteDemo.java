package aplicacion.domain.fuentesDemo;

import aplicacion.domain.fuentesMetamapa.FuenteMetamapa;
import aplicacion.domain.hechos.Hecho;

public class TestFuenteDemo {
    // Crear una fuente demo con una biblioteca de prueba que devuelva 2 hechos
    public static void main(String[] args) {
        Conexion conexionPrueba = new ConexionPrueba();
        FuenteDemo fuente = new FuenteDemo(conexionPrueba, "http://fake-url.com");

        fuente.pedirHechos();

        System.out.println("Hechos importados:");
        for (Hecho h : fuente.importarHechos()) {
            System.out.println(h.getTitulo() + " - " + h.getDescripcion() + " - " + h.getFechaAcontecimiento());
        }
/*
        FuenteMetamapa fuenteMetamapa= new FuenteMetamapa("http://localhost:8084/agregador/hechos");
        fuenteMetamapa.pedirHechos();
        System.out.println("Hechos importados:");
        for (Hecho h : fuente.importarHechos()) {
            System.out.println(h.getTitulo() + " - " + h.getDescripcion() + " - " + h.getFechaAcontecimiento());
        }*/
    }
}
/*Exception in thread "main" java.lang.ClassCastException: class java.lang.Integer cannot be cast to class aplicacion.domain.hechos.Categoria (java.lang.Integer is in module java.base of loader 'bootstrap'; aplicacion.domain.hechos.Categoria is in unnamed module of loader 'app')
        at aplicacion.domain.fuentesDemo.HechoBuilder.construirHecho(HechoBuilder.java:19)
        at aplicacion.domain.fuentesDemo.FuenteDemo.pedirHechos(FuenteDemo.java:44)
        at aplicacion.domain.fuentesDemo.TestFuenteDemo.main(TestFuenteDemo.java:12)*/

/*
{
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
