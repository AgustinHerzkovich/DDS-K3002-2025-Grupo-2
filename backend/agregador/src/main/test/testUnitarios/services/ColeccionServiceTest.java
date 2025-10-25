package testUnitarios.services;

import aplicacion.domain.algoritmos.AlgoritmoConsensoIrrestricto;
import aplicacion.domain.algoritmos.TipoAlgoritmoConsenso;
import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.ColeccionInputDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.mappers.*;
import aplicacion.dto.output.ColeccionOutputDto;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.repositorios.RepositorioDeColecciones;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.services.ColeccionService;
import aplicacion.services.FuenteService;
import aplicacion.services.HechoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColeccionServiceTest {

    @Mock private RepositorioDeColecciones repositorioDeColecciones;
    @Mock private RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    @Mock private HechoService hechoService;
    @Mock private ColeccionInputMapper coleccionInputMapper;
    @Mock private ColeccionOutputMapper coleccionOutputMapper;
    @Mock private HechoOutputMapper hechoOutputMapper;
    @Mock private FuenteService fuenteService;
    @Mock private FuenteInputMapper fuenteInputMapper;

    @InjectMocks private ColeccionService coleccionService;

    private Coleccion coleccion;
    private ColeccionInputDto inputDto;
    private ColeccionOutputDto outputDto;
    private Fuente fuente;

    @BeforeEach
    void setUp() {
        coleccion = new Coleccion("123", "Coleccion test", new ArrayList<>(), new ArrayList<>(), TipoAlgoritmoConsenso.IRRESTRICTO);

        fuente = new FuenteEstatica("f1", "localhost", 8082);
        coleccion.agregarFuente(fuente);

        inputDto = new ColeccionInputDto();
        outputDto = new ColeccionOutputDto();
    }

    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Debe guardar una colección correctamente")
    void guardarColeccionGuardaColeccion() {
        when(coleccionInputMapper.map(inputDto)).thenReturn(coleccion);
        when(repositorioDeColecciones.save(coleccion)).thenReturn(coleccion);
        when(coleccionOutputMapper.map(coleccion)).thenReturn(outputDto);

        ColeccionOutputDto resultado = coleccionService.guardarColeccion(inputDto);

        assertNotNull(resultado);
        verify(repositorioDeColecciones).save(coleccion);
        verify(coleccionOutputMapper).map(coleccion);
    }

    @Test
    @DisplayName("Debe asociar hechos preexistentes de todas las fuentes")
    void asociarHechosPreexistentesLlamaAsociacionPorFuente() {
        Fuente f2 = new FuenteEstatica(); f2.setId("f2");
        coleccion.setFuentes(List.of(fuente, f2));
        when(repositorioDeHechosXColeccion.saveAll(anyList())).thenReturn(List.of());
        when(fuenteService.obtenerHechosPorFuente(anyString())).thenReturn(List.of());

        coleccionService.asociarHechosPreexistentes(coleccion);

        verify(fuenteService, times(2)).obtenerHechosPorFuente(anyString());
    }

    @Test
    @DisplayName("Debe asociar hechos preexistentes de una fuente")
    void asociarHechosPreexistentesDeFuenteAColeccionGuardaHechosXColeccion() {
        Hecho hecho = new Hecho();
        when(fuenteService.obtenerHechosPorFuente(fuente.getId())).thenReturn(List.of(hecho));
        when(repositorioDeHechosXColeccion.saveAll(anyList())).thenReturn(List.of());

        coleccionService.asociarHechosPreexistentesDeFuenteAColeccion(coleccion, fuente);

        verify(repositorioDeHechosXColeccion).saveAll(anyList());
    }
    @Test
    @DisplayName("Debe obtener colecciones paginadas y mapear a DTO")
    void obtenerColeccionesDTODevolvuelvePageDeDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Coleccion> coleccionPage = new PageImpl<>(List.of(coleccion));
        when(repositorioDeColecciones.findAll(pageable)).thenReturn(coleccionPage);
        when(coleccionOutputMapper.map(coleccion)).thenReturn(outputDto);

        Page<ColeccionOutputDto> resultado = coleccionService.obtenerColeccionesDTO(pageable);

        assertEquals(1, resultado.getTotalElements());
        verify(coleccionOutputMapper).map(coleccion);
    }

    @Test
    @DisplayName("Debe obtener colección por ID o lanzar excepción")
    void obtenerColeccionDTODevuelveODaExcepcion() {
        when(repositorioDeColecciones.findById("123")).thenReturn(Optional.of(coleccion));
        when(coleccionOutputMapper.map(coleccion)).thenReturn(outputDto);

        ColeccionOutputDto resultado = coleccionService.obtenerColeccionDTO("123");

        assertNotNull(resultado);

        when(repositorioDeColecciones.findById("999")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> coleccionService.obtenerColeccionDTO("999"));
    }

    @Test
    @DisplayName("Debe obtener hechos irrestrictos por colección con texto libre")
    void obtenerHechosIrrestrictosPorColeccionObtieneHechosConTextoLibre() {
        when(hechoService.obtenerHechosPorColeccionYTextoLibre(anyString(), anyString()))
                .thenReturn(List.of(new Hecho()));
        when(hechoService.filtrarHechosQueryParam(anyList(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(new HechoOutputDto()));

        List<HechoOutputDto> resultado = coleccionService.obtenerHechosIrrestrictosPorColeccion(
                "123", null, null, null, null, null, null, null, "algo");

        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Debe eliminar una colección existente")
    void eliminarColeccionBorraColeccion() {
        when(repositorioDeColecciones.findById("123")).thenReturn(Optional.of(coleccion));

        coleccionService.eliminarColeccion("123");

        verify(hechoService).borrarHechosPorColeccion(coleccion);
        verify(repositorioDeColecciones).delete(coleccion);
    }

    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Debe modificar el algoritmo de consenso de la colección")
    void modificarAlgoritmoDeColeccionActualizaAlgoritmoYGuarda() {
        when(repositorioDeColecciones.findById("123")).thenReturn(Optional.of(coleccion));
        when(repositorioDeColecciones.save(coleccion)).thenReturn(coleccion);
        when(coleccionOutputMapper.map(coleccion)).thenReturn(outputDto);

        ColeccionOutputDto resultado = coleccionService.modificarAlgoritmoDeColeccion("123", TipoAlgoritmoConsenso.MAYORIA_SIMPLE);

        assertNotNull(resultado);
        verify(repositorioDeColecciones).save(coleccion);
    }

    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Debe agregar una fuente a una colección y asociar hechos preexistentes")
    void agregarFuenteAColeccionAgregaFuenteYGuardar() {
        FuenteInputDto fuenteInputDto = new FuenteInputDto();
        when(fuenteInputMapper.map(fuenteInputDto)).thenReturn(fuente);
        when(fuenteService.guardarFuenteSiNoExiste(fuente)).thenReturn(fuente);
        when(repositorioDeColecciones.findById("123")).thenReturn(Optional.of(coleccion));
        when(repositorioDeColecciones.save(coleccion)).thenReturn(coleccion);
        when(coleccionOutputMapper.map(coleccion)).thenReturn(outputDto);

        ColeccionOutputDto resultado = coleccionService.agregarFuenteAColeccion("123", fuenteInputDto);

        assertNotNull(resultado);
        verify(fuenteService).guardarFuenteSiNoExiste(fuente);
        verify(repositorioDeHechosXColeccion).saveAll(anyList());
    }

    // ------------------------------------------------------------------------
    @Test
    @DisplayName("Debe quitar una fuente de la colección y limpiar hechos si es la última")
    void quitarFuenteDeColeccionQuitaFuenteYEliminaHechos() throws Exception {
        when(repositorioDeColecciones.findById("123")).thenReturn(Optional.of(coleccion));
        when(fuenteService.obtenerFuentePorId("f1")).thenReturn(fuente);
        when(repositorioDeColecciones.save(coleccion)).thenReturn(coleccion);
        when(coleccionOutputMapper.map(coleccion)).thenReturn(outputDto);
        when(repositorioDeColecciones.existsByFuenteId("f1")).thenReturn(false);

        ColeccionOutputDto resultado = coleccionService.quitarFuenteDeColeccion("123", "f1");

        assertNotNull(resultado);
        verify(fuenteService).guardarFuente(fuente);
        verify(repositorioDeHechosXColeccion).deleteAllByFuenteId("f1");
    }
}
