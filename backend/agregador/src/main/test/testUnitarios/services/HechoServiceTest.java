package testUnitarios.services;

import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.repositorios.RepositorioDeHechos;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.services.ContribuyenteService;
import aplicacion.services.HechoService;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testUtils.HechoFactory;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HechoServiceTest
{

    @Mock
    private RepositorioDeHechos repositorioDeHechos;
    @Mock
    private RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    @Mock
    private HechoOutputMapper hechoOutputMapper;
    @Mock
    private NormalizadorDeHechos normalizadorDeHechos;
    @Mock
    private HechoInputMapper hechoInputMapper;
    @Mock
    private ContribuyenteService contribuyenteService;

    @InjectMocks
    private HechoService hechoService;

    private Hecho hecho;
    private HechoOutputDto hechoOutputDto;

    @BeforeEach
     void setUp() {
        hecho = HechoFactory.crearHechoAleatorio();
        hechoOutputDto = new HechoOutputDto();
    }

    @Test
    @DisplayName("Debe guardar el hecho y actualizar contribuyente")
    void guardarHechoGuardaHechoYActualizaContribuyente() {
        Contribuyente autor = new Contribuyente();
        hecho.setAutor(autor);

        Contribuyente autorActualizado = new Contribuyente();
        when(contribuyenteService.guardarOActualizar(autor)).thenReturn(autorActualizado);

        hechoService.guardarHecho(hecho);

        verify(contribuyenteService).guardarOActualizar(autor);
        verify(repositorioDeHechos).save(hecho);
        assertEquals(autorActualizado, hecho.getAutor());
    }
    @Test
    @DisplayName("Debe obtener hecho por ID")
    void obtenerHechoPorIdDevolvuelveHechoExistente() throws HechoNoEncontradoException {
        when(repositorioDeHechos.findByHechoId("abcd")).thenReturn(hecho);

        Hecho resultado = hechoService.obtenerHechoPorId("abcd");

        assertEquals(hecho, resultado);
        verify(repositorioDeHechos).findByHechoId("abcd");
    }
    @Test
    @DisplayName("Debe tirar excepcion si no existe el hecho buscado")
    void obtenerHechoPorIdTiraExcepcionSiNoExiste() {
        when(repositorioDeHechos.findByHechoId("inexistente")).thenThrow(new RuntimeException());

        assertThrows(HechoNoEncontradoException.class, () -> hechoService.obtenerHechoPorId("inexistente"));
    }
    @Test
    @DisplayName("Debe guardar hechos y devolver su DTO")
    void agregarHechoGuardaYDevuelveDto() {
        HechoInputDto inputDto = new HechoInputDto();
        when(hechoInputMapper.map(inputDto)).thenReturn(hecho);
        when(repositorioDeHechos.save(hecho)).thenReturn(hecho);
        when(hechoOutputMapper.map(hecho)).thenReturn(hechoOutputDto);

        HechoOutputDto resultado = hechoService.agregarHecho(inputDto);

        verify(normalizadorDeHechos).normalizar(hecho);
        verify(repositorioDeHechos).save(hecho);
        assertEquals(hechoOutputDto, resultado);
    }

    @Test
    @DisplayName("Debe guardar lista de hechos")
    void guardarHechosGuardaListaDeHechos() {
        List<Hecho> hechos = HechoFactory.crearHechosAleatorios(3);

        hechoService.guardarHechos(hechos);

        verify(repositorioDeHechos).saveAll(hechos);
    }
    @Test
    @DisplayName("Debe filtrar correctamente por categoria y fecha")
    void filtrarHechosQueryParamFiltraPorCategoriaYFecha() {
        Hecho hecho1 = HechoFactory.crearHechoAleatorio();
        hecho1.getCategoria().setNombre("Accidente");
        hecho1.setFechaCarga(LocalDateTime.now().minusDays(1));

        Hecho hecho2 = HechoFactory.crearHechoAleatorio();
        hecho2.getCategoria().setNombre("Otro");
        hecho2.setFechaCarga(LocalDateTime.now().minusDays(10));

        List<Hecho> hechos = List.of(hecho1, hecho2);
        when(hechoOutputMapper.map(hecho1)).thenReturn(new HechoOutputDto());

        List<HechoOutputDto> resultado = hechoService.filtrarHechosQueryParam(
                hechos,
                "Accidente",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now(),
                null,
                null,
                null,
                null
        );

        assertEquals(1, resultado.size());
        verify(hechoOutputMapper).map(hecho1);
    }

    @Test
    @DisplayName("Debe hallar hechos duplicados de una lista (Si se guardaron antes en la DB)")
    void hallarHechosDuplicadosDeListaDetectaDuplicados() {
        Hecho hecho1 = HechoFactory.crearHechoAleatorio();
        Hecho hecho2 = HechoFactory.crearHechoAleatorio();
        hecho1.getCategoria().setId(4321L);
        hecho2.getCategoria().setId(1234L);

        List<Hecho> lista = List.of(hecho1, hecho2, hecho2);

        List<Hecho> duplicados = hechoService.hallarHechosDuplicadosDeLista(lista);

        assertEquals(1, duplicados.size());
        assertEquals(hecho2, duplicados.getFirst());
    }
    @Test
    @DisplayName("Debe obtener hechos de un contribuyente determinado")
    void obtenerHechosDeContribuyentDevolvuelveHechosDelAutor() throws Exception {
        Hecho hecho = HechoFactory.crearHechoAleatorio();
        hecho.getAutor().setId(123L);
        Long contribuyenteId = hecho.getAutor().getId();
        when(contribuyenteService.obtenerContribuyente(contribuyenteId)).thenReturn(hecho.getAutor());
        when(repositorioDeHechos.findByAutorId(contribuyenteId)).thenReturn(List.of(hecho));
        when(hechoOutputMapper.map(hecho)).thenReturn(hechoOutputDto);

        List<HechoOutputDto> resultado = hechoService.obtenerHechosDeContribuyente(contribuyenteId);

        assertEquals(1, resultado.size());
        verify(contribuyenteService).obtenerContribuyente(contribuyenteId);
    }
}
