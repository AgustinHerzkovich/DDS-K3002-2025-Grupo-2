package testUnitarios.domain.hechos;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.multimedias.Video;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import testUtils.HechoFactory;
import testUtils.RandomThingsGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HechoTest {
    @Test
    public void sePuedeInstanciarUnHecho(){
        Assert.notNull(new Hecho("titulo", "descripcion", new Categoria("categoria"), new Ubicacion(2d, 2d), LocalDateTime.now(), Origen.DATASET, "contenidoTexto", List.of(new Video("mp4", 100, RandomThingsGenerator.generarLinkYoutubeAleatorio(), "720x1280", 2)), true, new Contribuyente(true, null, "mail")), "El hecho no se pudo crear");
    }
}
