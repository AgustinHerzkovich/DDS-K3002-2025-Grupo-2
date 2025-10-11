package aplicacion.controllers;

import aplicacion.dtos.*;
//import aplicacion.services.EstadisticasService;
import aplicacion.services.EstadisticasService;
import aplicacion.services.scheduler.ActualizacionEstadisticasScheduler;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aplicacion.utils.CSVConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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
            return ResponseEntity.badRequest().build();
        }
        List<ProvinciaConMasHechosDeColeccionDTO> dtos = fullListIfNull(idColeccion, estadisticasService::obtenerProvinciasConMasHechosDeUnaColeccion, page, limit, estadisticasService::obtenerTodasColeccionesDisponiblesIds);
        return sendWithFormat(dtos, accept, "provinciaConMasHechosDeColeccion");
    }

    @GetMapping(value = "/categoriasConMasHechos", produces = { MediaType.APPLICATION_JSON_VALUE, "text/csv" })
    public ResponseEntity<?> categoriaConMasHechosReportados(@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit, @RequestHeader(HttpHeaders.ACCEPT) String accept) {
        if(isPaginaYLimiteInvalid(page, limit)) {
            return ResponseEntity.badRequest().build();
        }
        List<CategoriaConMasHechosDTO> dtos =  estadisticasService.obtenerCategoriaConMasHechos(page, limit);
        return sendWithFormat(dtos, accept, "categoriasConMasHechos");
    }

    @GetMapping(value = "/provinciasConMasHechosDeCategoria", produces = { MediaType.APPLICATION_JSON_VALUE, "text/csv" })
    public ResponseEntity<?> provinciaConMasHechosDeCategoria(@RequestParam(value = "nombreCategoria", required = false) String nombreCategoria,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit, @RequestHeader(HttpHeaders.ACCEPT) String accept) {
        if(isPaginaYLimiteInvalid(page, limit)) {
            return ResponseEntity.badRequest().build();
        }
        List<ProvinciaConMasHechosDeCategoriaDTO> dtos = fullListIfNull(nombreCategoria, estadisticasService::obtenerProvinciaConMasHechosPorCategoria, page, limit, estadisticasService::obtenerTodasCategoriasDisponiblesIds);
        return sendWithFormat(dtos, accept, "provinciaConMasHechosDeCategoria");
    }

    @GetMapping(value = "/horaConMasHechosDeCategoria", produces = { MediaType.APPLICATION_JSON_VALUE, "text/csv" })
    public ResponseEntity<?> horaConMasHechosDeCategoria(@RequestParam(value = "nombreCategoria", required = false) String nombreCategoria,@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "1") Integer limit, @RequestHeader(HttpHeaders.ACCEPT) String accept) {
        if(isPaginaYLimiteInvalid(page, limit)) {
            return ResponseEntity.badRequest().build();
        }
        List<HoraConMasHechosDeCategoriaDTO> dtos = fullListIfNull(nombreCategoria, estadisticasService::obtenerHoraConMasHechosPorCategoria, page, limit, estadisticasService::obtenerTodasCategoriasDisponiblesIds);
        return sendWithFormat(dtos, accept, "horaConMasHechosDeCategoria");
    }

    @GetMapping(value = "/solicitudesDeEliminacionSpam", produces = { MediaType.APPLICATION_JSON_VALUE, "text/csv" })
    public ResponseEntity<?> solicitudesSpam(@RequestHeader(HttpHeaders.ACCEPT) String accept) {
        List<CantidadSolicitudesSpamDTO> cantidad = new ArrayList<>();
        cantidad.add(estadisticasService.obtenerCantidadSolicitudSpam());
        if(accept.contains("text/csv")) {
            try {
                String csvData = CSVConverter.convert(cantidad);
                return ResponseEntity.status(200).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "solicitudesDeEliminacionSpam" + ".csv")
                        .body(csvData);
            } catch (Exception e) {
                ResponseEntity.status(500).body("SE PUDRIO TODO");
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.ok(estadisticasService.obtenerCantidadSolicitudSpam());
    }

    @GetMapping("/coleccionesDisponibles")
    public ResponseEntity<List<ColeccionDisponibleDTO>> coleccionesDisponibles(@RequestParam(value = "page", defaultValue = "0") Integer page,@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        if(isPaginaYLimiteInvalid(page, limit)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(estadisticasService.obtenerColeccionesDisponibles(page, limit));
    }

    public boolean isPaginaYLimiteInvalid(Integer page, Integer limit){
        return !(page >= 0 && limit > 0);
    }


    public <T> List<T> fullListIfNull(String id, TriFunction<String, Integer, Integer, List<T>> obtenerCosas, Integer page, Integer limit, Supplier<List<String>> supplier) {
        List<T> dtos = new ArrayList<>();
        if (id == null) {
            List<String> identificadores = supplier.get();
            for (String identificador : identificadores) {
                dtos.addAll(obtenerCosas.apply(identificador, page, limit));
            }
        } else
            dtos.addAll(obtenerCosas.apply(id, page, limit));
        return dtos;
    }

    public ResponseEntity<?> sendWithFormat(List<?> dtos, String accept, String titulo) {
        try {
            if(accept.contains("text/csv")) {
                String csvData = CSVConverter.convert(dtos);
                return ResponseEntity.status(200).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + titulo + ".csv")
                        .body(csvData);
            }
            return ResponseEntity.ok(dtos);
        }catch (Exception e){
            throw new RuntimeException("No se pudo parsear el CSV");
        }
    }
}
