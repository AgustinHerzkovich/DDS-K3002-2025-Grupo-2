package aplicacion.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwsS3FileServerService {
    @Getter
    private final S3Client s3;
    private final String bucketName = "fuentes-estaticas";

    public AwsS3FileServerService() {
        String regionName = System.getenv("AWS_REGION");
        this.s3 = S3Client.builder()
                .region(Region.of(regionName)) // Lee AWS_REGION de variables de entorno
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()) // Lee AWS_ACCESS_KEY_ID=REDACTED
                .build();
    }

    // Crear bucket si no existe
    private void crearBucketSiNoExiste() {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3.headBucket(headBucketRequest);
        } catch (NoSuchBucketException e) {
            s3.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        }
    }

    // Subir archivo desde MultipartFile
    public void cargarArchivo(MultipartFile file) throws Exception {
        crearBucketSiNoExiste();
        String key = file.getOriginalFilename();

        if (existeArchivo(key)) {
            throw new IllegalStateException("Ya existe un archivo con el nombre: " + key);
        }

        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );
    }

    // Subir archivo desde InputStream
    public void cargarArchivoDesdeInputStream(InputStream inputStream,
                                              String nombreArchivo, String contentType) throws Exception {
        crearBucketSiNoExiste();

        if (existeArchivo(nombreArchivo)) {
            throw new IllegalStateException("Ya existe un archivo con el nombre: " + nombreArchivo);
        }

        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(nombreArchivo)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromInputStream(inputStream, inputStream.available()) // Usa available() para el tamaño, pero depende de que el mismo venga en el header de Content-Length
        );
    }

    // Obtener archivo como InputStream
    public InputStream obtenerArchivo(String nombreArchivo) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(nombreArchivo)
                .build();
        return s3.getObject(getObjectRequest);
    }

    public List<String> listarFuentes() {
        List<String> fuentes = new ArrayList<>();

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        s3.listObjectsV2Paginator(request)
                .stream()
                .flatMap(response -> response.contents().stream())
                .map(S3Object::key)
                .filter(key -> !key.endsWith("/")) // evita carpetas "vacías"
                .forEach(fuentes::add);

        return fuentes;
    }

    // Verifica si un archivo ya existe en el bucket
    private boolean existeArchivo(String key) {
        try {
            s3.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
            return true; // Si no lanza excepción, el archivo existe
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false; // No existe
            }
            throw e; // Otro error (permisos, etc.)
        }
    }
}