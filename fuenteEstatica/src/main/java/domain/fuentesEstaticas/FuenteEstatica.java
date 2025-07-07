package domain.fuentesEstaticas;

import domain.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

// FUENTE ESTATICA
public class FuenteEstatica extends Fuente {
    private final List<String> archivos;
    private final LectorCsv lectorArchivo;
    private Long id; // TODO automatizar para evitar repeticion de ids entre fuentes

    public FuenteEstatica(LectorCsv lectorArchivo, Long id){
        super(id);
        this.archivos = new ArrayList<>();
        this.lectorArchivo = lectorArchivo;
    }

    public void agregarArchivo(String archivo){
        archivos.add(archivo);
    }

    public List<Hecho> importarHechos() {
        List<Hecho> hechos = new ArrayList<>();
        archivos.forEach(archivo -> hechos.addAll(lectorArchivo.leerHechos(archivo)));
        return hechos;
    }
}