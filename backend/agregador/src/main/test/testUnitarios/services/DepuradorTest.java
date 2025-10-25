package testUnitarios.services;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.FuenteService;
import aplicacion.services.HechoService;
import aplicacion.services.depurador.DepuradorDeHechos;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import testUtils.HechoFactory;
import testUtils.RandomThingsGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepuradorTest {
    @Mock
    private HechoService hechoService;

    @Mock
    private FuenteService fuenteService;

    @InjectMocks
    private DepuradorDeHechos depuradorDeHechos;

    @Test
    @DisplayName("No debe tirar excepciones al ejecutarse")
    void noTiraExcepciones() {
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

    @Test
    @DisplayName("No debe guardar hechos repetidos")
    void evitaHechosRepetidos() {
        Fuente fuente = new FuenteEstatica();
        fuente.setId("fuente-test");
        fuente.setHechos(new ArrayList<>());



        List<Hecho> todosLosHechos = HechoFactory.crearHechosAleatorios(50);
        List<Hecho> repetidos = new ArrayList<>(todosLosHechos.subList(RandomThingsGenerator.generarEnteroAleatorio(30, 35), 40));
        List<Hecho> repetidosBD = new ArrayList<>(todosLosHechos.subList(RandomThingsGenerator.generarEnteroAleatorio(5, 10), 20));
        List<Hecho> hechos = new ArrayList<>(todosLosHechos);
        hechos.addAll(repetidos);
        List<Hecho> unicos = new ArrayList<>(todosLosHechos);
        unicos.removeAll(repetidosBD);
        unicos.removeAll(repetidos);
        unicos.addAll(repetidos);

        Map<Fuente, List<Hecho>> hechosPorFuente = Map.of(fuente, hechos);

        when(hechoService.hallarHechosDuplicadosDeBD(anyList())).thenReturn(new ArrayList<>(repetidosBD));
        when(hechoService.hallarHechosDuplicadosDeLista(anyList())).thenReturn(new ArrayList<>(repetidos));
        doAnswer(invocation -> {
            List<Hecho> listaOriginal = invocation.getArgument(0);
            List<Hecho> hechosAQuitar = invocation.getArgument(1);
            listaOriginal.removeIf(hechosAQuitar::contains);
            return null;
        }).when(hechoService).quitarHechosDeSublista(anyList(), anyList());


        depuradorDeHechos.depurar(hechosPorFuente);

        verify(hechoService).guardarHechos(
                argThat(lista -> lista.size() == unicos.size())
        );

        verify(fuenteService).guardarFuente(fuente);
    }
}