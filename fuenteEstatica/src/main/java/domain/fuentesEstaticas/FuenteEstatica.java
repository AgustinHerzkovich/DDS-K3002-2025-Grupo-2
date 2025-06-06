package domain.fuentesEstaticas;

import domain.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

// FUENTE ESTATICA
public class FuenteEstatica implements Fuente {
    private List<String> archivos;
    private LectorCsv lectorArchivo;

    public FuenteEstatica(LectorCsv lectorArchivo){
        this.archivos = new ArrayList<>();
        this.lectorArchivo = lectorArchivo;
    }

    public void agregarArchivo(String archivo){
        archivos.add(archivo);
    }

    public List<Hecho> importarHechos() {
        List<Hecho> hechos = new ArrayList<>();
        archivos.forEach(archivo -> hechos.addAll(lectorArchivo.leerHechos(10, archivo))); // Cantidad provisoria
        return hechos;
    }
}
