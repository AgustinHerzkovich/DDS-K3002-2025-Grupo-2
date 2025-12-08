package testUnitarios.services;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.input.ContribuyenteInputDto;
import aplicacion.dto.mappers.*;
import aplicacion.dto.output.ContribuyenteOutputDto;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;
import aplicacion.excepciones.MailYaExisteException;
import aplicacion.repositories.ContribuyenteRepository;
import aplicacion.services.ContribuyenteService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContribuyenteServiceTest {

    @Mock
    private ContribuyenteRepository repositorioDeContribuyentes;
    @Mock
    private ContribuyenteInputMapper contribuyenteInputMapper;
    @Mock
    private ContribuyenteOutputMapper contribuyenteOutputMapper;
    @Mock
    private IdentidadContribuyenteInputMapper identidadContribuyenteInputMapper;
    @Mock
    private IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper;

    @InjectMocks
    private ContribuyenteService contribuyenteService;

    private Contribuyente contribuyente;
    private ContribuyenteInputDto inputDto;
    private ContribuyenteOutputDto outputDto;

    @BeforeEach
    void setUp() {
        contribuyente = new Contribuyente(false, new IdentidadContribuyente("Juan", "Pérez", null), "juan@mail.com");
        contribuyente.setId(1L);
        inputDto = new ContribuyenteInputDto();
        inputDto.setMail("juan@mail.com");
        outputDto = new ContribuyenteOutputDto();
    }

    @Test
    @DisplayName("Debe guardar un contribuyente nuevo si el mail no existe")
    void guardarContribuyenteGuardaSiMailNoExiste() {
        when(repositorioDeContribuyentes.existsByMail(inputDto.getMail())).thenReturn(false);
        when(contribuyenteInputMapper.map(inputDto)).thenReturn(contribuyente);
        when(repositorioDeContribuyentes.save(contribuyente)).thenReturn(contribuyente);
        when(contribuyenteOutputMapper.map(contribuyente)).thenReturn(outputDto);

        ContribuyenteOutputDto resultado = contribuyenteService.guardarContribuyente(inputDto);

        assertNotNull(resultado);
        verify(repositorioDeContribuyentes).save(contribuyente);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el mail ya existe al guardar contribuyente")
    void guardarContribuyenteLanzaExcepcionSiMailExiste() {
        when(repositorioDeContribuyentes.existsByMail(inputDto.getMail())).thenReturn(true);

        assertThrows(MailYaExisteException.class, () -> contribuyenteService.guardarContribuyente(inputDto));
        verify(repositorioDeContribuyentes, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar contribuyente existente si el mail ya existe en BD")
    void guardarOActualizarActualizaSiExistePorMail() {
        Contribuyente existente = new Contribuyente(false, new IdentidadContribuyente("Ana", "García", null), "juan@mail.com");
        existente.setId(2L);
        when(repositorioDeContribuyentes.findByMail(contribuyente.getMail())).thenReturn(Optional.of(existente));
        when(repositorioDeContribuyentes.save(existente)).thenReturn(existente);

        Contribuyente resultado = contribuyenteService.guardarOActualizar(contribuyente);

        assertEquals(existente, resultado);
        verify(repositorioDeContribuyentes).save(existente);
        assertEquals(contribuyente.getIdentidad(), existente.getIdentidad());
    }

    @Test
    @DisplayName("Debe crear nuevo contribuyente si no existe por mail")
    void guardarOActualizarCreaSiNoExistePorMail() {
        when(repositorioDeContribuyentes.findByMail(contribuyente.getMail())).thenReturn(Optional.empty());
        when(repositorioDeContribuyentes.save(contribuyente)).thenReturn(contribuyente);

        Contribuyente resultado = contribuyenteService.guardarOActualizar(contribuyente);

        assertEquals(contribuyente, resultado);
        verify(repositorioDeContribuyentes).save(contribuyente);
    }

    @Test
    @DisplayName("Debe devolver contribuyente existente por ID e inicializar campos Hibernate")
    void obtenerContribuyentePorIdDevuelveExistente() {
        when(repositorioDeContribuyentes.findById(1L)).thenReturn(Optional.of(contribuyente));

        Contribuyente resultado = contribuyenteService.obtenerContribuyentePorId(1L);

        assertEquals(contribuyente, resultado);
        verify(repositorioDeContribuyentes, never()).save(any());
    }

    @Test
    @DisplayName("Debe crear contribuyente por defecto si no existe el ID")
    void obtenerContribuyentePorIdCreaNuevoSiNoExiste() {
        when(repositorioDeContribuyentes.findById(1L)).thenReturn(Optional.empty());
        when(repositorioDeContribuyentes.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Contribuyente resultado = contribuyenteService.obtenerContribuyentePorId(1L);

        assertNotNull(resultado);
        assertEquals("mailPorDefecto", resultado.getMail());
        verify(repositorioDeContribuyentes).save(any(Contribuyente.class));
    }

    @Test
    @DisplayName("Debe devolver contribuyente existente si está configurado")
    void obtenerContribuyenteDevuelveExistente() {
        when(repositorioDeContribuyentes.findById(1L)).thenReturn(Optional.of(contribuyente));

        Contribuyente resultado = contribuyenteService.obtenerContribuyente(1L);

        assertEquals(contribuyente, resultado);
    }

    @Test
    @DisplayName("Debe lanzar excepción si contribuyente no está configurado")
    void obtenerContribuyenteLanzaExcepcionSiNoExiste() {
        when(repositorioDeContribuyentes.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContribuyenteNoConfiguradoException.class, () -> contribuyenteService.obtenerContribuyente(1L));
    }

    @Test
    @DisplayName("Debe devolver DTO de contribuyente buscado por mail")
    void obtenerContribuyentePorMailDevuelveDto() {
        when(repositorioDeContribuyentes.findByMail("juan@mail.com")).thenReturn(Optional.of(contribuyente));
        when(contribuyenteOutputMapper.map(contribuyente)).thenReturn(outputDto);

        ContribuyenteOutputDto resultado = contribuyenteService.obtenerContribuyentePorMail("juan@mail.com");

        assertEquals(outputDto, resultado);
        verify(contribuyenteOutputMapper).map(contribuyente);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no se encuentra contribuyente por mail")
    void obtenerContribuyentePorMailLanzaExcepcionSiNoExiste() {
        when(repositorioDeContribuyentes.findByMail("noexiste@mail.com")).thenReturn(Optional.empty());

        assertThrows(ContribuyenteNoConfiguradoException.class, () ->
                contribuyenteService.obtenerContribuyentePorMail("noexiste@mail.com"));
    }

    @Test
    @DisplayName("Debe devolver lista de contribuyentes mapeados a DTO")
    void obtenerContribuyentesDevuelveListaDeDtos() {
        Contribuyente c2 = new Contribuyente(false, new IdentidadContribuyente("Ana", "García", null), "ana@mail.com");
        when(repositorioDeContribuyentes.findAll()).thenReturn(List.of(contribuyente, c2));
        when(contribuyenteOutputMapper.map(any())).thenReturn(outputDto);

        List<ContribuyenteOutputDto> resultado = contribuyenteService.obtenerContribuyentes();

        assertEquals(2, resultado.size());
        verify(contribuyenteOutputMapper, times(2)).map(any());
    }
}
