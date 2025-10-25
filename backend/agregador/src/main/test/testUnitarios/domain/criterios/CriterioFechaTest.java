package testUnitarios.domain.criterios;

import aplicacion.domain.criterios.CriterioDeFecha;
import aplicacion.domain.hechos.Hecho;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testUtils.HechoFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CriterioFechaTest {
    @Test
    @DisplayName("Debe filtrar correctamente")
    public void testCriterioFecha(){
        CriterioDeFecha criterioDeFecha = new CriterioDeFecha(LocalDateTime.now().minusYears(5), LocalDateTime.now().plusYears(5));
        Hecho hecho = HechoFactory.crearHechoAleatorio();
        hecho.setFechaAcontecimiento(LocalDateTime.now());
        assertTrue(() -> criterioDeFecha.cumpleCriterio(hecho));
        hecho.setFechaAcontecimiento(LocalDateTime.now().plusYears(6));
        assertFalse(criterioDeFecha.cumpleCriterio(hecho));
        hecho.setFechaAcontecimiento(LocalDateTime.now().plusYears(4));
        assertTrue(criterioDeFecha.cumpleCriterio(hecho));
        hecho.setFechaAcontecimiento(LocalDateTime.now().minusYears(6));
        assertFalse(criterioDeFecha.cumpleCriterio(hecho));
        hecho.setFechaAcontecimiento(LocalDateTime.now().minusYears(3));
        assertTrue(criterioDeFecha.cumpleCriterio(hecho));
    }
}
