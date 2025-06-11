package domain.fuentesEstaticas;

import domain.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

// FUENTE ESTATICA
public class FuenteEstatica implements Fuente {
    private List<String> archivos;
    private LectorCsv lector_archivo;
    private Long id; // TODO automatizar para evitar repeticion de ids entre fuentes

    public FuenteEstatica(LectorCsv lector_archivo, Long id){
        this.archivos = new ArrayList<>();
        this.lector_archivo = lector_archivo;
        this.id = id;
    }

    public void agregarArchivo(String archivo){
        archivos.add(archivo);
    }

    public List<Hecho> importarHechos() {
        List<Hecho> hechos = new ArrayList<>();
        archivos.forEach(archivo -> hechos.addAll(lector_archivo.leerHechos(archivo)));
        return hechos;
    }

    public Long getId(){
        return id;
    }
}