package aplicacion.services.depurador;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.clasesIntermedias.HechoXFuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.HechoService;
import aplicacion.excepciones.HechoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DepuradorDeHechos {
    private final HechoService hechoService;

    public DepuradorDeHechos(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    public void depurar(Map<Fuente, List<Hecho>> hechosPorFuente) {
        for (Map.Entry<Fuente, List<Hecho>> entry: hechosPorFuente.entrySet()) {
            Fuente fuente = entry.getKey();
            System.out.println("Depurando hechos de la fuente: " + fuente.getId() + " con " + entry.getValue().size() + " hechos.");
            List<Hecho> hechos = entry.getValue();

            long inicioFuente = System.nanoTime();

            List<Hecho> hechosDuplicadosDeBD = hechoService.hallarHechosDuplicadosDeBD(hechos);
            long finHallarDuplicados = System.nanoTime();
            System.out.println(hechosDuplicadosDeBD.size() + " hechos duplicados encontrados");


            // Saca los que ya estan en la BD de lista "hechos"
            hechoService.quitarHechosSegunCodigoUnico(hechos, hechosDuplicadosDeBD);
            long finFiltrarDuplicados = System.nanoTime();
            System.out.println("Hechos duplicados removidos");


            // Usa los que se van a crear
            List<HechoXFuente> hechosXFuenteAInsertarUnicos = hechos.stream().map(hecho -> new HechoXFuente(hecho, fuente)).toList();
            // Usa los de la BD
            List<HechoXFuente> hechosXFuenteAInsertarRepetidos = hechosDuplicadosDeBD.stream().map(hecho -> new HechoXFuente(hecho, fuente)).toList();

            long finCrearHechosXFuente = System.nanoTime();
            System.out.println("HechosXFuente creados");


            // No guarda los repetidos
            hechoService.guardarHechos(hechos);

            long finGuardarNuevosHechos = System.nanoTime();
            System.out.println("Hechos nuevos guardados");

            hechoService.guardarHechosPorFuente(hechosXFuenteAInsertarUnicos);
            hechoService.guardarHechosPorFuente(hechosXFuenteAInsertarRepetidos);

            long finFuente = System.nanoTime();
            System.out.println("Hechos X Fuente guardados");

            // Cálculo de tiempos
            long tiempoTotal = finFuente - inicioFuente;
            long tBuscarDuplicados = finHallarDuplicados - inicioFuente;
            long tFiltrarDuplicados = finFiltrarDuplicados - finHallarDuplicados;
            long tCrearHXF = finCrearHechosXFuente - finFiltrarDuplicados;
            long tGuardarNuevos = finGuardarNuevosHechos - finCrearHechosXFuente;
            long tGuardarHXF = finFuente - finGuardarNuevosHechos;

            System.out.println("Fin depuración de la fuente: " + fuente.getId() + ". Tiempo: " + tiempoTotal /1_000_000 + " ms.");
            try {
                System.out.println("    Tasa de depuración: " + (hechos.size() / (tiempoTotal/1_000_000_000.0)) + " hechos/segundo.");
                System.out.printf("     ️ Buscar duplicados:         %4.0f ms     (%5.1f%%)%n", tBuscarDuplicados / 1_000_000.0, (100.0 * tBuscarDuplicados / tiempoTotal));
                System.out.printf("     ️ Filtrar duplicados:        %4.0f ms     (%5.1f%%)%n", tFiltrarDuplicados / 1_000_000.0, (100.0 * tFiltrarDuplicados / tiempoTotal));
                System.out.printf("     ️ Crear HechoXFuente:        %4.0f ms     (%5.1f%%)%n", tCrearHXF / 1_000_000.0, (100.0 * tCrearHXF / tiempoTotal));
                System.out.printf("     ️ Guardar nuevos Hechos:     %4.0f ms     (%5.1f%%)%n", tGuardarNuevos / 1_000_000.0, (100.0 * tGuardarNuevos / tiempoTotal));
                System.out.printf("     ️ Guardar HechosXFuente:     %4.0f ms     (%5.1f%%)%n", tGuardarHXF / 1_000_000.0, (100.0 * tGuardarHXF / tiempoTotal));
            }catch (Exception ignored){}
        }
    }
}
