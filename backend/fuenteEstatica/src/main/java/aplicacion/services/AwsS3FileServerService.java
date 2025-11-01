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
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()) // Lee AWS_ACCESS_KEY_ID y AWS_SECRET_ACCESS_KEY de variables de entorno
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
    public void cargarArchivo(String carpeta, MultipartFile file) throws Exception {
        crearBucketSiNoExiste();
        String key = carpeta + "/" + file.getOriginalFilename();
        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );
    }

    // Subir archivo desde InputStream
    public void cargarArchivoDesdeInputStream(String carpeta, InputStream inputStream,
                                              String nombreArchivo, String contentType) throws Exception {
        crearBucketSiNoExiste();
        String key = carpeta + "/" + nombreArchivo;
        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromInputStream(inputStream, inputStream.available()) // Usa available() para el tamaño, pero depende de que el mismo venga en el header de Content-Length
        );
    }

    // Listar archivos en una "carpeta" (prefijo)
    public List<String> listarArchivos(String carpeta) {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(carpeta + "/")
                .build();

        List<String> archivos = new ArrayList<>();
        // Usa `listObjectsV2Paginator` para manejar la paginación automáticamente
        s3.listObjectsV2Paginator(request).stream()
                .flatMap(response -> response.contents().stream())
                .map(S3Object::key)
                .filter(key -> !key.endsWith("/")) // Excluye las "carpetas"
                .forEach(key -> archivos.add(key.substring(carpeta.length() + 1)));

        return archivos;
    }

    // Obtener archivo como InputStream
    public InputStream obtenerArchivo(String carpeta, String nombreArchivo) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(carpeta + "/" + nombreArchivo)
                .build();
        return s3.getObject(getObjectRequest);
    }

    // Mover archivo entre "carpetas"
    public void moverArchivo(String carpetaOrigen, String nombreArchivo,
                             String carpetaDestino, String nombreDestino) {
        String sourceKey = carpetaOrigen + "/" + nombreArchivo;
        String destKey = carpetaDestino + "/" + nombreDestino;

        String copySource = bucketName + "/" + sourceKey;

        CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                .copySource(copySource)
                .destinationBucket(bucketName)
                .destinationKey(destKey)
                .build();

        s3.copyObject(copyRequest);

        // Borra el objeto original
        s3.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(sourceKey)
                .build()
        );
    }
    public List<String> listarFuentes() {
        List<String> fuentes = new ArrayList<>();

        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket("fuentes-estaticas")
                .delimiter("/")
                .build();

        s3.listObjectsV2Paginator(request)
                .stream()
                .flatMap(response -> response.commonPrefixes().stream())
                .map(CommonPrefix::prefix)
                .map(prefix -> prefix.replaceAll("/$", "")) // Quita la barra final
                .map(fuente -> fuente.substring(6)) // Saca "fuente"
                .forEach(fuentes::add);

        return fuentes;
    }
}