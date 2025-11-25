package testUnitarios.services;

import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.repositories.HechoRepository;
import aplicacion.repositories.HechoXColeccionRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import testUtils.ContribuyenteFactory;
import testUtils.HechoFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HechoServiceTest
{

    @Mock
    private HechoRepository repositorioDeHechos;
    @Mock
    private HechoXColeccionRepository repositorioDeHechosXColeccion;
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
        when(repositorioDeHechos.findById("abcd")).thenReturn(Optional.of(hecho));

        Hecho resultado = hechoService.obtenerHechoPorId("abcd");

        assertEquals(hecho, resultado);
        verify(repositorioDeHechos).findById("abcd");
    }
    @Test
    @DisplayName("Debe tirar excepcion si no existe el hecho buscado")
    void obtenerHechoPorIdTiraExcepcionSiNoExiste() {
        when(repositorioDeHechos.findById("inexistente")).thenReturn(Optional.empty());

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
    void obtenerHechosDeContribuyentDevolvuelveHechosDelAutor() {
        Hecho hecho = HechoFactory.crearHechoAleatorio();
        hecho.setAutor(ContribuyenteFactory.crearContribuyenteAleatorio());
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
