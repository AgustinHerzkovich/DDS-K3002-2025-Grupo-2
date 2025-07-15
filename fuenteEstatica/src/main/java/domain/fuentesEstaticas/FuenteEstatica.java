package domain.fuentesEstaticas;

import domain.lectores.LectorArchivo;
import domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// FUENTE ESTATICA
@Entity
public class FuenteEstatica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @ElementCollection
    @CollectionTable(name = "fuente_estatica_archivos")
    @Column(name = "ruta_archivo")
    private  List<String> archivos;

    public FuenteEstatica() {
        this.archivos = new ArrayList<>();
    }

    public void agregarArchivo(String rutaRelativa){
        archivos.add(rutaRelativa);
    }

    public List<Hecho> importarHechos(Function<String, Optional<LectorArchivo>> proveedorLector) {
        List<Hecho> hechos = new ArrayList<>();
        for (String rutaRelativa : archivos) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(rutaRelativa)) {
                if (is == null) {
                    System.out.println("No se encontró el archivo: " + rutaRelativa);
                    continue;
                }

                String extension = obtenerExtension(rutaRelativa);
                Optional<LectorArchivo> lectorOpt = proveedorLector.apply(extension);
                if (lectorOpt.isEmpty()) {
                    System.out.println("No hay lector disponible para extensión: " + extension);
                    continue;
                }

                hechos.addAll(lectorOpt.get().leerHechos(is));
            } catch (IOException e) {
                System.out.println("Error al abrir el archivo: " + e.getMessage());
            }
        }
        return hechos;
    }

    private String obtenerExtension(String ruta) {
        int i = ruta.lastIndexOf('.');
        return (i >= 0) ? ruta.substring(i + 1) : "";
    }
}