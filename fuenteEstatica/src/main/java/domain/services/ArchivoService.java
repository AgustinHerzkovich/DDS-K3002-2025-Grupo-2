package domain.services;

import domain.lectores.LectorArchivo;
import domain.lectores.LectorCsv;
import domain.hechos.Hecho;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArchivoService {

    private final FileServerService fileServerService;

    public ArchivoService(FileServerService fileServerService) {
        this.fileServerService = fileServerService;
    }

    private final String BUCKET_PENDIENTES = "pendientes";
    private final String BUCKET_PROCESADOS = "procesados";

    // Subir archivo a pendientes
    public void subirArchivoPendiente(MultipartFile file) throws Exception {
        fileServerService.cargarArchivo(BUCKET_PENDIENTES, file);
    }

    public void subirArchivoDesdeUrl(String urlString) throws Exception {
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true); // sigue redirecciones
        connection.connect();

        String path = urlString.split("\\?")[0];

        String fileName = path.substring(path.lastIndexOf('/') + 1);

        try (InputStream is = connection.getURL().openStream()) {
            fileServerService.cargarArchivoDesdeInputStream(BUCKET_PENDIENTES, is, fileName, "application/octet-stream");
            System.out.println("Archivo subido correctamente a MinIO: " + fileName);
        }finally {
            connection.disconnect();
        }
    }
    // Leer todos los archivos pendientes y generar hechos
    public List<Hecho> leerHechosPendientesConFechaMayorA(LocalDateTime fecha) {
        List<Hecho> hechos = new ArrayList<>();
        try {
            List<String> archivos = fileServerService.listarArchivos(BUCKET_PENDIENTES);

            for (String archivo : archivos) {
                try (InputStream is = fileServerService.obtenerArchivo(BUCKET_PENDIENTES, archivo)) {
                    String extension = obtenerExtension(archivo);
                    lectorParaExtension(extension)
                            .ifPresent(lector -> hechos.addAll(lector.leerHechos(is)));
                }
                // Mover archivo a procesados
                fileServerService.moverArchivo(BUCKET_PENDIENTES, archivo, BUCKET_PROCESADOS, archivo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo archivos pendientes: " + e.getMessage());
        }

        if (fecha != null) {
            return hechos.stream()
                    .filter(hecho -> hecho.seCargoDespuesDe(fecha))
                    .collect(Collectors.toList());
        }

        return hechos;
    }

    private Optional<LectorArchivo> lectorParaExtension(String extension) {
        return switch (extension.toLowerCase()) {
            case "csv" -> Optional.of(new LectorCsv());
            default -> Optional.empty();
        };
    }

    private String obtenerExtension(String nombreArchivo) {
        int i = nombreArchivo.lastIndexOf('.');
        return (i >= 0) ? nombreArchivo.substring(i + 1) : "";
    }
}