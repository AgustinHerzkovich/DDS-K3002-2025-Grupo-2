package aplicacion.services;

import aplicacion.domain.lectores.LectorArchivo;
import aplicacion.domain.lectores.LectorCsv;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.HechoOutputDto;
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
    private final HechoOutputMapper hechoOutputMapper;

    public ArchivoService(AwsS3FileServerService fileServerService, HechoOutputMapper hechoOutputMapper) {
        this.fileServerService = fileServerService;
        this.hechoOutputMapper = hechoOutputMapper;
    }

    // Subir archivo al fileserver
    public void subirArchivo(String fuenteId, MultipartFile file) throws Exception {
        fileServerService.cargarArchivo("fuente" + fuenteId, file);
    }

    public void subirArchivoDesdeUrl(String fuenteId, String urlString) throws Exception {
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
                    "fuente" + fuenteId,
                    new ByteArrayInputStream(data),
                    fileName,
                    "application/octet-stream"
            );

            System.out.println("Archivo subido correctamente a S3: " + fileName);
        } finally {
            connection.disconnect();
        }
    }

    public List<HechoOutputDto> leerHechosConFechaMayorA(String fuenteId, LocalDateTime fecha) {
        return this.leerHechos(fuenteId).stream().filter(hecho -> hecho.getFechaCarga().isAfter(fecha)).collect(Collectors.toList());
    }

    // Leer todos los archivos y generar hechos
    public List<HechoOutputDto> leerHechos(String fuenteId) {
        List<Hecho> hechos = new ArrayList<>();
        String carpeta = "fuente" + fuenteId;
        try {
            List<String> archivos = fileServerService.listarArchivos(carpeta);

            for (String archivo : archivos) {
                try (InputStream is = fileServerService.obtenerArchivo(carpeta, archivo)) {
                    String extension = obtenerExtension(archivo);
                    lectorParaExtension(extension)
                            .ifPresent(lector -> hechos.addAll(lector.leerHechos(is)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo archivos: " + e.getMessage());
        }

        return hechos.stream().map(hechoOutputMapper::map).toList();
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