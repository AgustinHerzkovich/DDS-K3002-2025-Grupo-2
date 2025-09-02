package aplicacion.services.normalizador;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;

import java.util.*;

public class NormalizadorDeTerminos {
    List<Termino> terminosConocidos = new ArrayList<>();
    @Getter @Setter
    private Integer umbral = null;
    LevenshteinDetailedDistance levenshtein;
    public NormalizadorDeTerminos(Integer umbral){
        this.umbral = umbral;
        LevenshteinDetailedDistance levenshtein = new LevenshteinDetailedDistance(umbral);
    }

    // Dado un String, devuelve el mismo si esta admitido, su sinónimo si este toma precedencia, o alguno cercano segun distancia de Levenshtein

    public String normalizarTermino(String textoANormalizar){
        if(umbral == null){
            throw new UmbralNoDefinidoException("Umbral es null en NormalizadorDeTerminos");
        }
        return  normalizarTermino(textoANormalizar, umbral);
    }
    // Normaliza dado un umbral personalizado
    public String normalizarTermino(String textoANormalizar, Integer umbralDeNormalizacion) {
        Optional<Termino> mejorMatch = hallarMejorMatch(textoANormalizar, umbralDeNormalizacion); // Si no lo encuentra, usa levenshtein
        // Ninguno cumple con el umbral
        // Devuelve el string del termino, o si es sinonimo, al termino que apunta
        return mejorMatch.map(Termino::normalizar).orElse(null);
    }

    // Agrega un termino como admitido por el normalizador
    public void agregarTermino(String nombre) {
        Termino nuevoTermino = new Termino(nombre);
        terminosConocidos.add(nuevoTermino);
    }

    // Agrega un termino como sinonimo de otro. Si el primero no existe como termino, tira NoSuchElementException
    public void agregarSinonimo(String terminoRaiz, String sinonimo) throws NoSuchElementException {
        Termino nuevoSinonimo = new Termino(sinonimo, terminosConocidos.stream().filter(t -> t.coincide(terminoRaiz)).findFirst().get());
        terminosConocidos.add(nuevoSinonimo);
    }

    // Aplica el algoritmo de hallar el mejor match
    private Optional<Termino> hallarMejorMatch(String terminoAComparar, Integer umbralDeNormalizacion) {
        Termino mejor = null;
        int mejorDistancia = Integer.MAX_VALUE;
        for (Termino t : terminosConocidos) {
            Integer distancia = levenshtein.apply(t.getNombre(), terminoAComparar).getDistance();
            if (distancia == null)
                continue;    // Si no llega al umbral continua (no tira NullPointerException ni nada raro)
            if (distancia < mejorDistancia) {
                mejor = t;
                mejorDistancia = distancia;
                if (distancia == 0) break; //Si es exacto, se detiene
            }
        }
        return Optional.ofNullable(mejor);  // Tira optional que puede ser null si ninguno cumple con el umbral
    }
}

