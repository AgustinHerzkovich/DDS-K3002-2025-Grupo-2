package testUnitarios.domain.hechos;

import aplicacion.domain.hechos.*;
import aplicacion.domain.usuarios.Contribuyente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import testUtils.HechoFactory;
import testUtils.RandomThingsGenerator;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class HechoTest {
    Hecho hecho = HechoFactory.crearHechoAleatorio();

    @Test
    @DisplayName("Debe poderse instanciar")
    public void sePuedeInstanciarUnHecho(){
        new Hecho("titulo", "descripcion", new Categoria("categoria"), new Ubicacion(2d, 2d), LocalDateTime.now(), Origen.DATASET, "contenidoTexto", List.of(new Multimedia(RandomThingsGenerator.generarLinkYoutubeAleatorio())), true, new Contribuyente(true, null, "mail"));
    }
    @Test
    @DisplayName("Debe generar una clave unica")
    public void generaClaveUnica(){
        hecho.getCategoria().setId(123L);
        hecho.getClaveUnica();
    }
    @Test
    @DisplayName("No debe generar una clave unica si no se guardo en la BD")
    public void noGeneraClaveUnica(){
        // No se puede crear si no normalizo categoria (Se hace al normalizar)
        assertThrows(NullPointerException.class ,() -> hecho.getClaveUnica());
    }
    @Test
    @DisplayName("Debe iniciar siendo visible")
    public void iniciaSiendoVisible(){
        Assert.isTrue(hecho.esVisible(), "No es visible");
    }
    @Test
    @DisplayName("Debe poderse mostrarse y ocultarse")
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
    @DisplayName("Debe poderse etiquetar")
    public void esEtiquetable(){
        hecho.etiquetar(new Etiqueta("Incendio"));
        hecho.etiquetar(new Etiqueta("Catástrofe"));
        Assert.isTrue(hecho.contieneEtiqueta(new Etiqueta("Incendio")), "No tiene etiquetas agregadas");
        Assert.isTrue(!hecho.contieneEtiqueta(new Etiqueta("Catastrofe")), "No distingue tildes");
    }
}
