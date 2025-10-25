package testUnitarios.services;

import aplicacion.domain.hechos.Etiqueta;
import aplicacion.excepciones.EtiquetaNoEncontradaException;
import aplicacion.repositorios.RepositorioDeEtiquetas;
import aplicacion.services.EtiquetaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtiquetaServiceTest {

    @Mock
    private RepositorioDeEtiquetas repositorioDeEtiquetas;

    @InjectMocks
    private EtiquetaService etiquetaService;

    private Etiqueta etiqueta;

    @BeforeEach
    void setUp() {
        etiqueta = new Etiqueta("Violencia");
    }

    @Test
    @DisplayName("Debe devolver una etiqueta existente por nombre")
    void obtenerEtiquetaPorNombreDevuelveEtiquetaExistente() throws EtiquetaNoEncontradaException {
        when(repositorioDeEtiquetas.findByNombre("Violencia")).thenReturn(Optional.of(etiqueta));

        Etiqueta resultado = etiquetaService.obtenerEtiquetaPorNombre("Violencia");

        assertEquals(etiqueta, resultado);
        verify(repositorioDeEtiquetas).findByNombre("Violencia");
    }

    @Test
    @DisplayName("Debe lanzar excepción si la etiqueta no existe")
    void obtenerEtiquetaPorNombreLanzaExcepcionSiNoExiste() {
        when(repositorioDeEtiquetas.findByNombre("Inexistente")).thenReturn(Optional.empty());

        assertThrows(EtiquetaNoEncontradaException.class, () -> etiquetaService.obtenerEtiquetaPorNombre("Inexistente"));
    }

    @Test
    @DisplayName("Debe agregar una nueva etiqueta correctamente")
    void agregarEtiquetaGuardaEtiqueta() {
        when(repositorioDeEtiquetas.save(any(Etiqueta.class))).thenReturn(etiqueta);

        Etiqueta resultado = etiquetaService.agregarEtiqueta("Violencia");

        assertEquals("Violencia", resultado.getNombre());
        verify(repositorioDeEtiquetas).save(any(Etiqueta.class));
    }
}
