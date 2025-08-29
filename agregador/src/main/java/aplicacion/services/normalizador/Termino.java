package aplicacion.services.normalizador;

import lombok.Getter;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;

import java.util.Objects;

public class Termino {
    @Getter
    private String nombre;
    private Termino refiereA; // Si no es null es "sinonimo", de lo contrario es "raiz"

    public Termino(String nombre) {
        this.nombre = nombre;
        this.refiereA = null;
    }

    public Termino(String nombre, Termino sinonimoDe) {
        this.nombre = nombre;
        this.refiereA = sinonimoDe;
    }

    public String normalizar() {
        return refiereA == null ? nombre : refiereA.normalizar();
    }

    public Boolean coincide(String otroTermino) {
        return Objects.equals(nombre, otroTermino);
    }

    public Boolean coincide(Termino otroTermino) {
        return Objects.equals(nombre, otroTermino.nombre);
    }

    public Integer distanciaLevenshtein(String termino, Integer umbral) {
        //TODO: threshold configurable
        return new LevenshteinDetailedDistance(umbral).apply(nombre, termino).getDistance();
    }
}
