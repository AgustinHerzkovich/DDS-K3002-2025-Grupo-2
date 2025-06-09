package domain;

import domain.fuentesEstaticas.FuenteEstatica;
import domain.fuentesEstaticas.LectorCsv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FuenteEstaticaAplicacion {

  public static void main(String[] args) {

    SpringApplication.run(FuenteEstaticaAplicacion.class, args);

    FuenteEstatica fuente = new FuenteEstatica(new LectorCsv());

    String path_alternativo = "src/test/ArchivosCsvPrueba/";

    fuente.agregarArchivo(path_alternativo + "desastres_naturales_argentina.csv");
    fuente.agregarArchivo(path_alternativo + "desastres_sanitarios_contaminacion_argentina.csv");
    //fuente.agregarArchivo(path_alternativo + "desastres_sanitarios_contaminacion_argentina.xlsx");
    fuente.agregarArchivo(path_alternativo + "desastres_tecnologicos_argentina.csv");

    fuente.importarHechos();
  }

}