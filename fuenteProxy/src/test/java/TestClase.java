import domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
import domain.hechos.Hecho;
import org.junit.Test;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestClase {
    @Test
    public void prueba() {
        FuenteMetamapa fuente = new FuenteMetamapa(1L);
        List<Hecho> hechos = fuente.importarHechos();
        for (Hecho h : hechos) {
            System.out.println("Titulo: " + h.getTitulo());
            System.out.println("Descripcion: " + h.getDescripcion());
            System.out.println("Categoria: " + h.getCategoria().getNombre());
            System.out.println("Latitud: " + h.getUbicacion().getLatitud().toString());
            System.out.println("Longitud: " + h.getUbicacion().getLongitud().toString());
            System.out.println("Fecha del hecho: " + h.getFechaAcontecimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Ubicacion latitud: "+h.getUbicacion().getLatitud());
            System.out.println("Ubicacion longitud: "+h.getUbicacion().getLongitud());
        }
    }
}
