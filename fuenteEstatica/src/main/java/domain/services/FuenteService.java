package domain.services;

import domain.fuentesEstaticas.FuenteEstatica;
import domain.lectores.LectorArchivo;
import domain.lectores.LectorCsv;
import domain.hechos.Hecho;
import domain.repositorios.RepositorioDeFuentes;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FuenteService {
    private final RepositorioDeFuentes repositorioDeFuentes;

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes) {
        this.repositorioDeFuentes = repositorioDeFuentes;
    }

    private Optional<LectorArchivo> lectorParaExtension(String extension) {
        return switch (extension.toLowerCase()) {
            case "csv" -> Optional.of(new LectorCsv());
            // case "xlsx" -> Optional.of(new LectorExcel());
            // case "json" -> Optional.of(new LectorJson());
            default -> Optional.empty();
        };
    }

    public List<Hecho> obtenerTodosLosHechosConFechaMayorA(LocalDateTime fechaMayorA) {
        List<Hecho> hechos = new ArrayList<>();
        repositorioDeFuentes.findAll().forEach(fuente ->
                hechos.addAll(fuente.importarHechos(this::lectorParaExtension))
        );

        if (fechaMayorA == null) return hechos;

        return hechos.stream()
                .filter(hecho -> hecho.seCargoDespuesDe(fechaMayorA))
                .collect(Collectors.toList());
    }

    public List<Hecho> obtenerHechosPorFuenteConFechaMayorA(Long id, LocalDateTime fechaMayorA) {
        FuenteEstatica fuente = repositorioDeFuentes.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la fuente con id " + id));

        List<Hecho> hechos = fuente.importarHechos(this::lectorParaExtension);

        if (fechaMayorA == null) return hechos;

        return hechos.stream()
                .filter(hecho -> hecho.seCargoDespuesDe(fechaMayorA))
                .collect(Collectors.toList());
    }

    public FuenteEstatica crearFuenteEstatica(List<String> archivos) {
        FuenteEstatica nuevaFuente = new FuenteEstatica();
        archivos.forEach(nombreArchivo -> {
            String rutaRelativa = "ArchivosCsvPrueba/" + nombreArchivo;
            nuevaFuente.agregarArchivo(rutaRelativa);
        });
        return repositorioDeFuentes.save(nuevaFuente);
    }
}