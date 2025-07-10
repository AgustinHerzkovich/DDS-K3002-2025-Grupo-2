package domain.fuentesEstaticas;

import domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

// FUENTE ESTATICA
@Entity
@NoArgsConstructor
public class FuenteEstatica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @ElementCollection
    @CollectionTable(name = "fuente_estatica_archivos")
    @Column(name = "ruta_archivo")
    private  List<String> archivos;
    @Transient
    private LectorCsv lectorArchivo;

    public FuenteEstatica(LectorCsv lectorArchivo) {
        this.lectorArchivo = lectorArchivo;
        this.archivos = new ArrayList<>();
    }

    public void agregarArchivo(String rutaRelativa){
        archivos.add(rutaRelativa);
    }

    public List<Hecho> importarHechos() {
        if (lectorArchivo == null) {
            lectorArchivo = new LectorCsv(); // Inicialización lazy
        }
        List<Hecho> hechos = new ArrayList<>();
        archivos.forEach(rutaRelativa -> {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(rutaRelativa)) {
                if (is == null) {
                    System.out.println("No se encontró el archivo: " + rutaRelativa);
                    return;
                }
                hechos.addAll(lectorArchivo.leerHechos(is));
            } catch (IOException e) {
                System.out.println("Error al abrir el archivo: " + e.getMessage());
            }
        });
        return hechos;
    }
}