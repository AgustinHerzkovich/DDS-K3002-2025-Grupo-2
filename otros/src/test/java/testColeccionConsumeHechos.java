import domain.colecciones.Coleccion;
import domain.hechos.Hecho;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class testColeccionConsumeHechos {
    @Test
    public void verHechosFuenteEstatica() {
        Coleccion coleccion = new Coleccion("Desastres naturales", "capo");

        System.out.println(coleccion.getIdentificador_handle());

        List<Hecho> hechos = coleccion.mostrarHechos();

        assertEquals(34617, hechos.size());

        for (Hecho h : hechos) {
            System.out.println("Titulo: " + h.getTitulo());
            System.out.println("Descripcion: " + h.getDescripcion());
            System.out.println("Categoria: " + h.getCategoria().getNombre());
            System.out.println("Latitud: " + h.getUbicacion().getLatitud().toString());
            System.out.println("Longitud: " + h.getUbicacion().getLongitud().toString());
            System.out.println("Fecha del hecho: " + h.getFecha_acontecimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println();
        }
    }

    @Test
    public void verHechosFuenteDinamica() {
        Coleccion coleccion = new Coleccion("Incendio Forestal", "capo");

        System.out.println(coleccion.getIdentificador_handle());

        List<Hecho> hechos = coleccion.mostrarHechos();

        assertEquals(1, hechos.size());

        for (Hecho h : hechos) {
            System.out.println("Titulo: " + h.getTitulo());
            System.out.println("Descripcion: " + h.getDescripcion());
            System.out.println("Categoria: " + h.getCategoria().getNombre());
            System.out.println("Latitud: " + h.getUbicacion().getLatitud().toString());
            System.out.println("Longitud: " + h.getUbicacion().getLongitud().toString());
            System.out.println("Fecha del hecho: " + h.getFecha_acontecimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println();
        }

    }
}
