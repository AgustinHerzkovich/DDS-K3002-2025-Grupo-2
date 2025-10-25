package testUnitarios.domain.criterios;

import aplicacion.domain.criterios.CriterioDeDistancia;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.hechos.Ubicacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import testUtils.HechoFactory;

public class CriterioDistanciaTest {
    @Test
    @DisplayName("Debe filtrar correctamente")
    public void testCriterioDeDistancia() {
        // Origen: Buenos Aires
        Ubicacion origen = new Ubicacion(-34.6, -58.4);
        // Otras ubicaciones aproximadas
        Ubicacion ubi1 = new Ubicacion(-34.8, -58.5); // ~20 km (cerca)
        Ubicacion ubi2 = new Ubicacion(-33.9, -60.6); // ~200 km (Rosario)
        Ubicacion ubi3 = new Ubicacion(-31.4, -64.2); // ~650 km (Córdoba)
        Ubicacion ubi4 = new Ubicacion(-32.9, -60.7); // ~300 km (Santa Fe)
        Ubicacion ubi5 = new Ubicacion(-38.0, -57.6); // ~385 km (Mar del Plata)
        Ubicacion ubi6 = new Ubicacion(-40.8, -63.0); // ~800 km (casi se pasa, Viedma)
        Ubicacion ubi7 = new Ubicacion(-41.1, -71.3); // ~1300 km (Bariloche, fuera del rango)

        // Criterio: dentro de 800 km
        CriterioDeDistancia crit = new CriterioDeDistancia(origen, 800d);

        Hecho hecho = HechoFactory.crearHechoAleatorio();

        hecho.setUbicacion(origen);
        Assert.isTrue(!crit.cumpleCriterio(hecho), "origen cumple criterio de distancia");

        hecho.setUbicacion(ubi1);
        Assert.isTrue(!crit.cumpleCriterio(hecho), "ubi1 cumple criterio de distancia");

        hecho.setUbicacion(ubi2);
        Assert.isTrue(!crit.cumpleCriterio(hecho), "ubi2 cumple criterio de distancia");

        hecho.setUbicacion(ubi3);
        Assert.isTrue(!crit.cumpleCriterio(hecho), "ubi3 cumple criterio de distancia");

        hecho.setUbicacion(ubi4);
        Assert.isTrue(!crit.cumpleCriterio(hecho), "ubi4 cumple criterio de distancia");

        hecho.setUbicacion(ubi5);
        Assert.isTrue(!crit.cumpleCriterio(hecho), "ubi5 cumple criterio de distancia");

        hecho.setUbicacion(ubi6);
        Assert.isTrue(!crit.cumpleCriterio(hecho), "ubi6 cumple criterio de distancia");

        hecho.setUbicacion(ubi7);
        Assert.isTrue(crit.cumpleCriterio(hecho), "ubi7 no cumple criterio de distancia");
    }
}
