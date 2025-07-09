package domain.fuentesEstaticas;

import domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    }

    public void agregarArchivo(String archivo){
        archivos.add(archivo);
    }


    public List<Hecho> importarHechos() {
        if (lectorArchivo == null) {
            lectorArchivo = new LectorCsv(); // Inicialización lazy
        }
        List<Hecho> hechos = new ArrayList<>();
        archivos.forEach(archivo -> hechos.addAll(lectorArchivo.leerHechos(archivo)));
        return hechos;

    }
}