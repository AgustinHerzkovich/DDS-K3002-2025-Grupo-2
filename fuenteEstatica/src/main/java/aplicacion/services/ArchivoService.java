package aplicacion.services;

import aplicacion.domain.lectores.LectorArchivo;
import aplicacion.domain.lectores.LectorCsv;
import aplicacion.domain.hechos.Hecho;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArchivoService {
    private final AwsS3FileServerService fileServerService;
    private final String CARPETA_PENDIENTES = "pendientes";
    private final String CARPETA_PROCESADOS = "procesados";

    public ArchivoService(AwsS3FileServerService fileServerService) {
        this.fileServerService = fileServerService;
    }

    // Subir archivo a pendientes
    public void subirArchivoPendiente(Long fuenteId, MultipartFile file) throws Exception {
        fileServerService.cargarArchivo("fuente" + fuenteId + "/" + CARPETA_PENDIENTES, file);
    }

    public void subirArchivoDesdeUrl(Long fuenteId, String urlString) throws Exception {
        urlString = urlString.trim().replaceAll("^\"|\"$", "");

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true);
        connection.connect();

        // Obtener el nombre del archivo desde la URL
        String path = url.getPath();
        String fileName = path.substring(path.lastIndexOf('/') + 1);

        try (InputStream is = connection.getInputStream()) {
            // Leer en memoria para conocer el tamaño exacto
            byte[] data = is.readAllBytes();

            // Subir a S3 usando FileServerService
            fileServerService.cargarArchivoDesdeInputStream(
                    "fuente" + fuenteId + "/" + CARPETA_PENDIENTES,
                    new ByteArrayInputStream(data),
                    fileName,
                    "application/octet-stream"
            );

            System.out.println("Archivo subido correctamente a S3: " + fileName);
        } finally {
            connection.disconnect();
        }
    }

    // Leer todos los archivos pendientes y generar hechos
    public List<Hecho> leerHechosPendientesConFechaMayorA(Long fuenteId, LocalDateTime fecha) {
        List<Hecho> hechos = new ArrayList<>();
        String carpetaPendientes = "fuente" + fuenteId + "/" + CARPETA_PENDIENTES;
        String carpetaProcesados = "fuente" + fuenteId + "/" + CARPETA_PROCESADOS;
        try {
            List<String> archivos = fileServerService.listarArchivos(carpetaPendientes);

            for (String archivo : archivos) {
                try (InputStream is = fileServerService.obtenerArchivo(carpetaPendientes, archivo)) {
                    String extension = obtenerExtension(archivo);
                    lectorParaExtension(extension)
                            .ifPresent(lector -> hechos.addAll(lector.leerHechos(is)));
                }
                // Mover archivo a procesados
                fileServerService.moverArchivo(carpetaPendientes, archivo, carpetaProcesados, archivo);
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