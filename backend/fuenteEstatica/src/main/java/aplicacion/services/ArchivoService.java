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
    public void subirArchivo(MultipartFile file) throws Exception {
        fileServerService.cargarArchivo(file);
    }

    public void subirArchivoDesdeUrl(String urlString) throws Exception {
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
                    new ByteArrayInputStream(data),
                    fileName,
                    "application/octet-stream"
            );

            System.out.println("Archivo subido correctamente a S3: " + fileName);
        } finally {
            connection.disconnect();
        }
    }

    public List<HechoOutputDto> leerHechosConFechaMayorA(String fuente, LocalDateTime fecha) {
        return this.leerHechos(fuente).stream().filter(hecho -> hecho.getFechaCarga().isAfter(fecha)).collect(Collectors.toList());
    }

    // Leer la fuente y generar hechos
    public List<HechoOutputDto> leerHechos(String fuente) {
        List<Hecho> hechos = new ArrayList<>();
        try {
            InputStream is = fileServerService.obtenerArchivo(fuente);
            String extension = obtenerExtension(fuente);
            lectorParaExtension(extension).ifPresent(lector -> hechos.addAll(lector.leerHechos(is)));
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