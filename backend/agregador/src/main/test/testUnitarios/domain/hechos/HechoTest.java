package testUnitarios.domain.hechos;

import aplicacion.domain.hechos.*;
import aplicacion.domain.hechos.multimedias.Video;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import domain.dashboardDTOs.hechos.HechoBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import testUtils.HechoFactory;
import testUtils.RandomThingsGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class HechoTest {
    Hecho hecho = HechoFactory.crearHechoAleatorio();

    @Test
    public void sePuedeInstanciarUnHecho(){
        Assert.notNull(new Hecho("titulo", "descripcion", new Categoria("categoria"), new Ubicacion(2d, 2d), LocalDateTime.now(), Origen.DATASET, "contenidoTexto", List.of(new Video("mp4", 100, RandomThingsGenerator.generarLinkYoutubeAleatorio(), "720x1280", 2)), true, new Contribuyente(true, null, "mail")), "El hecho no se pudo crear");
    }
    @Test
    public void generaClaveUnica(){
        // No se puede crear si no normalizo categoria (Se hace al normalizar)
        assertThrows(NullPointerException.class ,() -> hecho.getClaveUnica());
    }
    @Test
    public void iniciaSiendoVisible(){
        Assert.isTrue(hecho.esVisible(), "No es visible");
    }
    @Test
    public void seMuestraYOculta(){
        Assert.isTrue(hecho.esVisible(), "No es visible");
        hecho.ocultar();
        Assert.isTrue(!hecho.esVisible(), "Es visible");
        hecho.mostrar();
        Assert.isTrue(hecho.esVisible(), "No es visible");
        hecho.ocultar();
        Assert.isTrue(!hecho.esVisible(), "Es visible");
        hecho.mostrar();
        Assert.isTrue(hecho.esVisible(), "No es visible");
    }
    @Test
    public void esEtiquetable(){
        hecho.etiquetar(new Etiqueta("Incendio"));
        hecho.etiquetar(new Etiqueta("Catástrofe"));
        Assert.isTrue(hecho.contieneEtiqueta(new Etiqueta("Incendio")), "No tiene etiquetas agregadas");
        Assert.isTrue(!hecho.contieneEtiqueta(new Etiqueta("Catastrofe")), "No distingue tildes");
    }
}
