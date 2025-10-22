import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.FuenteService;
import aplicacion.services.HechoService;
import aplicacion.services.depurador.DepuradorDeHechos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testUtils.HechoFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestDepurador {

    @Mock
    private HechoService hechoService;

    @Mock
    private FuenteService fuenteService;

    @InjectMocks
    private DepuradorDeHechos depuradorDeHechos;

    @Test
    void testDepurar() {
        Fuente fuente = new FuenteEstatica();
        fuente.setId("fuente-test");
        fuente.setHechos(new ArrayList<>());

        List<Hecho> hechos = HechoFactory.crearHechosAleatorios(50);
        Map<Fuente, List<Hecho>> hechosPorFuente = Map.of(fuente, hechos);

        when(hechoService.hallarHechosDuplicadosDeBD(anyList())).thenReturn(List.of());
        when(hechoService.hallarHechosDuplicadosDeLista(anyList())).thenReturn(List.of());

        depuradorDeHechos.depurar(hechosPorFuente);

        verify(hechoService).hallarHechosDuplicadosDeBD(hechos);
        verify(hechoService).hallarHechosDuplicadosDeLista(hechos);
        verify(hechoService, times(2)).quitarHechosDeSublista(hechos, List.of());
        verify(hechoService).guardarHechos(hechos);
        verify(fuenteService).guardarFuente(fuente);
    }
}