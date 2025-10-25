package testUnitarios.services;

import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.solicitudes.SolicitudEliminacion;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.SolicitudInputDto;
import aplicacion.dto.mappers.EstadoSolicitudOutputMapper;
import aplicacion.dto.mappers.SolicitudOutputMapper;
import aplicacion.dto.output.SolicitudOutputDto;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.excepciones.MotivoSolicitudException;
import aplicacion.repositorios.RepositorioDeSolicitudes;
import aplicacion.services.ContribuyenteService;
import aplicacion.services.HechoService;
import aplicacion.services.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import testUtils.ContribuyenteFactory;
import testUtils.HechoFactory;
import testUtils.RandomThingsGenerator;
import testUtils.SolicitudFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private RepositorioDeSolicitudes repositorioDeSolicitudes;
    @Mock
    private HechoService hechoService;
    @Mock
    private ContribuyenteService contribuyenteService;
    @Mock
    private SolicitudOutputMapper solicitudOutputMapper;

    @InjectMocks
    private SolicitudService solicitudService;

    private Hecho hecho;
    private SolicitudEliminacion solicitud;

    @BeforeEach
    void setUp() {
        hecho = HechoFactory.crearHechoAleatorio();
        solicitud = new SolicitudFactory().generarSolicitudAleatoria(hecho);
        solicitud.setId(1L);
    }

    @Test
    @DisplayName("Debe obtener una solicitud por ID correctamente")
    void obtenerSolicitudDevuelveSolicitud() {
        when(repositorioDeSolicitudes.findById(1L)).thenReturn(Optional.of(solicitud));

        SolicitudEliminacion result = solicitudService.obtenerSolicitud(1L);

        assertEquals(solicitud, result);
        verify(repositorioDeSolicitudes).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no existe solicitud con ID dado")
    void obtenerSolicitudLanzaExcepcionSiNoExiste() {
        when(repositorioDeSolicitudes.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> solicitudService.obtenerSolicitud(99L));
    }

    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Debe obtener DTO de solicitud por ID")
    void obtenerSolicitudDTOMapeaCorrectamente() {
        SolicitudOutputDto dto = new SolicitudOutputMapper(new EstadoSolicitudOutputMapper()).map(solicitud);
        when(repositorioDeSolicitudes.findById(1L)).thenReturn(Optional.of(solicitud));
        when(solicitudOutputMapper.map(solicitud)).thenReturn(dto);

        SolicitudOutputDto resultado = solicitudService.obtenerSolicitudDTO(1L);

        assertEquals(dto, resultado);
        verify(solicitudOutputMapper).map(solicitud);
    }

    @Test
    @DisplayName("Debe devolver lista de solicitudes como DTOs")
    void obtenerSolicitudesDTOMapeaLista() {
        SolicitudOutputDto dto = new SolicitudOutputMapper(new EstadoSolicitudOutputMapper()).map(solicitud);
        when(repositorioDeSolicitudes.findAll()).thenReturn(List.of(solicitud));
        when(solicitudOutputMapper.map(solicitud)).thenReturn(dto);

        List<SolicitudOutputDto> resultado = solicitudService.obtenerSolicitudesDTO();

        assertEquals(1, resultado.size());
        verify(solicitudOutputMapper).map(solicitud);
    }

    @Test
    @DisplayName("Debe guardar una solicitud y asociarla al hecho")
    @Transactional
    void guardarSolicitudGuardarYAsociaHecho() throws HechoNoEncontradoException {
        when(hechoService.obtenerHechoPorId(hecho.getId())).thenReturn(hecho);
        when(repositorioDeSolicitudes.save(solicitud)).thenReturn(solicitud);

        SolicitudEliminacion resultado = solicitudService.guardarSolicitud(solicitud);

        verify(repositorioDeSolicitudes).save(solicitud);
        verify(hechoService).guardarHecho(hecho);
        assertEquals(solicitud, resultado);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el motivo de solicitud es demasiado corto")
    void guardarSolicitudDtoLanzaExcepcionPorMotivoCorto() {
        SolicitudInputDto dto = new SolicitudInputDto(1234L, "abc", "Motivo corto < 500 caracteres");

        assertThrows(MotivoSolicitudException.class, () -> solicitudService.guardarSolicitudDto(dto));
    }

    @Test
    @DisplayName("Debe guardar solicitud DTO correctamente si el motivo es válido")
    void guardarSolicitudDtoGuardaCorrectamente() throws Exception {

        Contribuyente contribuyente = ContribuyenteFactory.crearContribuyenteAleatorio();
        SolicitudInputDto dto = new SolicitudInputDto(contribuyente.getId(), hecho.getId(), RandomThingsGenerator.generarMotivoEliminacionLargo());

        when(hechoService.obtenerHechoPorId(dto.getHechoId())).thenReturn(hecho);
        when(contribuyenteService.obtenerContribuyentePorId(dto.getSolicitanteId())).thenReturn(contribuyente);
        when(repositorioDeSolicitudes.save(any(SolicitudEliminacion.class))).thenReturn(solicitud);
        when(solicitudOutputMapper.map(any())).thenReturn( new SolicitudOutputMapper(new EstadoSolicitudOutputMapper()).map(solicitud));

        SolicitudOutputDto resultado = solicitudService.guardarSolicitudDto(dto);

        assertNotNull(resultado);
        verify(repositorioDeSolicitudes).save(any(SolicitudEliminacion.class));
        verify(hechoService).guardarHecho(hecho);
    }

    @Test
    @DisplayName("Debe devolver solicitudes relacionadas con un mismo hecho")
    void solicitudesRelacionadasDevuelveSolicitudesRelacionadas() {
        when(repositorioDeSolicitudes.findById(1L)).thenReturn(Optional.of(solicitud));
        when(repositorioDeSolicitudes.findByHecho(hecho)).thenReturn(List.of(solicitud));

        List<SolicitudEliminacion> resultado = solicitudService.solicitudesRelacionadas(1L);

        assertEquals(1, resultado.size());
        verify(repositorioDeSolicitudes).findByHecho(hecho);
    }

    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Debe lanzar excepción si se pide solicitudes relacionadas con ID inexistente")
    void solicitudesRelacionadasLanzaExcepcionSiNoExiste() {
        when(repositorioDeSolicitudes.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> solicitudService.solicitudesRelacionadas(99L));
    }

    @Test
    @DisplayName("Debe cambiar el estado de una solicitud a ACEPTADA")
    void actualizarEstadoSolicitudCambiaEstadoAAceptada() {
        SolicitudEliminacion solMock = mock(SolicitudEliminacion.class);
        solicitudService.actualizarEstadoSolicitud(solMock, "ACEPTADA");
        verify(solMock).aceptar(null);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el estado es inválido")
    void actualizarEstadoSolicitudLanzaExcepcionPorEstadoInvalido() {
        SolicitudEliminacion solMock = mock(SolicitudEliminacion.class);
        assertThrows(IllegalArgumentException.class, () ->
                solicitudService.actualizarEstadoSolicitud(solMock, "INVALIDO"));
    }
}
