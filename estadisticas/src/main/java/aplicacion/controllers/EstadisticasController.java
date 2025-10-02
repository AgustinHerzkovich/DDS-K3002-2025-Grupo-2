package aplicacion.controllers;

import aplicacion.dtos.*;
//import aplicacion.services.EstadisticasService;
import aplicacion.services.EstadisticasService;
import aplicacion.services.scheduler.ActualizacionEstadisticasScheduler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aplicacion.utils.CSVConverter;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {
    ActualizacionEstadisticasScheduler actualizacionEstadisticasScheduler;
    EstadisticasService estadisticasService;

    public EstadisticasController(ActualizacionEstadisticasScheduler actualizacionEstadisticasScheduler, EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
        this.actualizacionEstadisticasScheduler = actualizacionEstadisticasScheduler;
    }


    @PostMapping("/actualizar")
    public ResponseEntity<Void> actualizarEstadisticas() {
        System.out.println("Actualizando estadisticas...");
        actualizacionEstadisticasScheduler.actualizarEstadisticas(); // Tarea scheduleada de inmediato
        System.out.println("""
                ---------------------------------
                    Estadisticas actualizadas.
                ---------------------------------
                """);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/provinciasConMasHechosDeColeccion", produces = { MediaType.APPLICATION_JSON_VALUE, "text/csv" })
    public ResponseEntity<?> provinciasDeColeccion(@RequestParam(value = "idColeccion", required = false) String idColeccion,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit, @RequestHeader(HttpHeaders.ACCEPT) String accept) throws Exception {
        if(isPaginaYLimiteInvalid(page, limit)) {
            System.out.println("Pagina o limite invalidos");
            return ResponseEntity.badRequest().build();
        }
        List<ProvinciaConMasHechosDeColeccionDTO> dtos = new ArrayList<>();

        if (idColeccion == null) {
            List<String> coleccionesIds = estadisticasService.obtenerTodasColeccionesDisponiblesIds();
            for (String coleccionId : coleccionesIds) {
                dtos.addAll(estadisticasService.obtenerProvinciasConMasHechosDeUnaColeccion(coleccionId, page, limit));
            }
        }else
            dtos.addAll(estadisticasService.obtenerProvinciasConMasHechosDeUnaColeccion(idColeccion, page, limit));

        if(accept.contains("text/csv")) {
            String csvData = CSVConverter.convert(dtos);
            return ResponseEntity.status(200).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=provinciaConMasHechosDeColeccion.csv")
                    .body(csvData);
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/categoriasConMasHechos")
    public ResponseEntity<List<CategoriaConMasHechosDTO>> categoriaConMasHechosReportados(@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerCategoriaConMasHechos(page, limit));
    }

    @GetMapping("/provinciasConMasHechosDeCategoria")
    public ResponseEntity<List<ProvinciaConMasHechosDTO>> provinciaConMasHechosDeCategoria(@RequestParam("nombreCategoria") String nombreCategoria,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerProvinciaConMasHechosPorCategoria(nombreCategoria, page, limit));
    }

    @GetMapping("/horaConMasHechosDeCategoria")
    public ResponseEntity<List<HoraConMasHechosDeCategoriaDTO>> horaConMasHechosDeCategoria(@RequestParam("nombreCategoria") String nombreCategoria,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerHoraConMasHechosPorCategoria(nombreCategoria, page, limit));
    }

    @GetMapping("/solicitudesDeEliminacionSpam")
    public ResponseEntity<CantidadSolicitudesSpamDTO> solicitudesSpam() {
        return ResponseEntity.ok(estadisticasService.obtenerCantidadSolicitudSpam());
    }

    @GetMapping("/coleccionesDisponibles")
    public ResponseEntity<List<ColeccionDisponibleDTO>> coleccionesDisponibles(@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(estadisticasService.obtenerColeccionesDisponibles(page, limit));
    }

    public boolean isPaginaYLimiteInvalid(Integer page, Integer limit){
        return !(page >= 0 && limit > 0);
    }
}
