import domain.fuentesEstaticas.FuenteEstatica;
import domain.fuentesEstaticas.LectorCsv;
import domain.hechos.Hecho;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.format.DateTimeFormatter;

import java.util.List;

public class testCargarArchivos {
    @Test
    public void cargarArchivos() {
        FuenteEstatica fuente = new FuenteEstatica(new LectorCsv(), 1L);

        String path_alternativo = "src/main/resources/ArchivosCsvPrueba/";

        fuente.agregarArchivo(path_alternativo + "desastres_naturales_argentina.csv");
        fuente.agregarArchivo(path_alternativo + "desastres_sanitarios_contaminacion_argentina.csv");
        fuente.agregarArchivo(path_alternativo + "desastres_tecnologicos_argentina.csv");

        List<Hecho> hechos = fuente.importarHechos();

        assertEquals(34617, hechos.size());

        for (Hecho h : hechos) {
            System.out.println("Titulo: " + h.getTitulo());
            System.out.println("Descripcion: " + h.getDescripcion());
            System.out.println("Categoria: " + h.getCategoria().getNombre());
            System.out.println("Latitud: " + h.getUbicacion().getLatitud().toString());
            System.out.println("Longitud: " + h.getUbicacion().getLongitud().toString());
            System.out.println("Fecha del hecho: " + h.getFechaAcontecimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println();
        }
    }
}