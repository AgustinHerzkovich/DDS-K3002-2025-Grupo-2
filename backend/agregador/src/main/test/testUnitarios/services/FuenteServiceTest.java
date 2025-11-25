package testUnitarios.services;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.FuenteInputMapper;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.repositories.FuenteRepository;
import aplicacion.services.FuenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuenteServiceTest {

    @Mock
    private FuenteRepository repositorioDeFuentes;

    @Mock
    private FuenteInputMapper fuenteInputMapper;

    @Mock
    private HechoInputMapper hechoInputMapper;

    @InjectMocks
    private FuenteService fuenteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe guardar una fuente correctamente")
    void guardarFuenteGuardaFuente() {
        Fuente fuente = new FuenteEstatica();
        when(repositorioDeFuentes.save(fuente)).thenReturn(fuente);

        Fuente resultado = fuenteService.guardarFuente(fuente);

        assertEquals(fuente, resultado);
        verify(repositorioDeFuentes, times(1)).save(fuente);
    }

    @Test
    @DisplayName("Debe guardar fuente si no existe y devolver existente si ya está")
    void guardarFuenteSiNoExisteGuardaFuenteSiNoExisteYDevuelveLaExistenteDeLoContrario() {
        Fuente fuenteNueva = new FuenteEstatica(); fuenteNueva.setId("f1");
        Fuente fuenteExistente = new FuenteEstatica(); fuenteExistente.setId("f1");

        when(repositorioDeFuentes.findById("f1")).thenReturn(Optional.of(fuenteExistente));

        Fuente resultado = fuenteService.guardarFuenteSiNoExiste(fuenteNueva);

        assertSame(fuenteExistente, resultado);
        verify(repositorioDeFuentes, never()).save(any());
    }

    @Test
    @DisplayName("Debe guardar fuente si no existe en el repositorio")
    void guardarFuenteSiNoExisteGuardarNueva() {
        Fuente fuenteNueva = new FuenteEstatica(); fuenteNueva.setId("f1");
        when(repositorioDeFuentes.findById("f1")).thenReturn(Optional.empty());
        when(repositorioDeFuentes.save(fuenteNueva)).thenReturn(fuenteNueva);

        Fuente resultado = fuenteService.guardarFuenteSiNoExiste(fuenteNueva);

        assertEquals(fuenteNueva, resultado);
        verify(repositorioDeFuentes, times(1)).save(fuenteNueva);
    }

    @Test
    @DisplayName("Debe mapear y guardar lista de fuentes")
    void guardarFuentesMapeaYGuardaListaDeFuentes() {
        FuenteInputDto dto = new FuenteInputDto();
        Fuente fuente = new FuenteEstatica();
        when(fuenteInputMapper.map(dto)).thenReturn(fuente);
        when(repositorioDeFuentes.save(fuente)).thenReturn(fuente);

        fuenteService.guardarFuentes(List.of(dto));

        verify(repositorioDeFuentes, times(1)).save(fuente);
    }

    @Test
    @DisplayName("Debe devolver cantidad de fuentes")
    void obtenerCantidadFuentesDevuelveCantidadDeFuentes() {
        when(repositorioDeFuentes.count()).thenReturn(10L);

        Long count = fuenteService.obtenerCantidadFuentes();

        assertEquals(10L, count);
        verify(repositorioDeFuentes, times(1)).count();
    }

    @Test
    @DisplayName("Debe devolver lista de hechos por id de fuente")
    void obtenerHechosPorFuenteDevuelveHechosPorIdDeFuente() {
        List<Hecho> hechos = List.of(new Hecho());
        when(repositorioDeFuentes.findHechosByFuenteId("f1")).thenReturn(hechos);

        List<Hecho> resultado = fuenteService.obtenerHechosPorFuente("f1");

        assertEquals(hechos, resultado);
        verify(repositorioDeFuentes).findHechosByFuenteId("f1");
    }

    @Test
    @DisplayName("Debe obtener fuente por id o lanzar excepción si no existe")
    void obtenerFuentePorIdObtieneFuenteOTiraExcepcion() throws FuenteNoEncontradaException {
        Fuente fuente = new FuenteEstatica(); fuente.setId("f1");
        when(repositorioDeFuentes.findById("f1")).thenReturn(Optional.of(fuente));
        when(repositorioDeFuentes.findById("f2")).thenReturn(Optional.empty());

        Fuente encontrada = fuenteService.obtenerFuentePorId("f1");
        assertEquals(fuente, encontrada);

        assertThrows(FuenteNoEncontradaException.class, () -> fuenteService.obtenerFuentePorId("f2"));
    }

    @Test
    @DisplayName("Debe mapear hechos de la última petición correctamente")
    void hechosUltimaPeticionDevuelveHechosDeUltimaPeticion() {
        Fuente fuente = mock(Fuente.class);
        when(fuente.getId()).thenReturn("f1");
        HechoInputDto hechoDto = new HechoInputDto();
        Hecho hecho = new Hecho();

        when(fuente.getHechosUltimaPeticion()).thenReturn(List.of(hechoDto));
        when(hechoInputMapper.map(hechoDto)).thenReturn(hecho);
        when(repositorioDeFuentes.save(fuente)).thenReturn(fuente);

        Map<Fuente, List<Hecho>> resultado = fuenteService.hechosUltimaPeticion(List.of(fuente));

        assertTrue(resultado.containsKey(fuente));
        assertEquals(1, resultado.get(fuente).size());
        verify(repositorioDeFuentes, times(1)).save(fuente);
    }
}
