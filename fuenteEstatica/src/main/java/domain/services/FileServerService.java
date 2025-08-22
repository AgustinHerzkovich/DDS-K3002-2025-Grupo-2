package domain.services;

import io.minio.messages.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServerService {

    private final MinioClient minioClient;

    public FileServerService() {
        this.minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("miniouser", "miniopassword")
                .build();
    }

    // Crear bucket si no existe
    public void crearBucketSiNoExiste(String bucket) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    // Subir archivo a un bucket
    public void cargarArchivo(String bucket, MultipartFile file) throws Exception {
        crearBucketSiNoExiste(bucket);

        String objectName = file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    // Obtener archivo como InputStream
    public InputStream obtenerArchivo(String bucket, String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
    }

    // Listar todos los objetos de un bucket
    public List<String> listarArchivos(String bucket) throws Exception {
        List<String> archivos = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .build()
        );
        for (Result<Item> result : results) {
            archivos.add(result.get().objectName());
        }
        return archivos;
    }

    // Mover archivo de un bucket a otro
    public void moverArchivo(String bucketOrigen, String objectName,
                             String bucketDestino, String objectDestino) throws Exception {
        crearBucketSiNoExiste(bucketDestino);

        // Copiar al bucket destino
        minioClient.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder()
                                .bucket(bucketOrigen)
                                .object(objectName)
                                .build())
                        .bucket(bucketDestino)
                        .object(objectDestino)
                        .build()
        );

        // Borrar del bucket origen
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketOrigen)
                        .object(objectName)
                        .build()
        );
    }
}