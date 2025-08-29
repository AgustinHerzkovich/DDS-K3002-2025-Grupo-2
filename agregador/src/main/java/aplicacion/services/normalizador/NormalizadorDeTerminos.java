package aplicacion.services.normalizador;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class NormalizadorDeTerminos {
    List<Termino> terminosConocidos = new ArrayList<>();
    @Getter @Setter
    Integer umbral = null;

    public NormalizadorDeTerminos(Integer umbral){
        this.umbral = umbral;
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
        Optional<Termino> mejorMatch = hallarMejorMatch(textoANormalizar, umbralDeNormalizacion);

        if (mejorMatch.isEmpty()) {      // Ninguno cumple con el umbral
            throw new NingunTerminoCumpleUmbralException("Ningun termino cumple con el umbral de "+umbralDeNormalizacion);
        } else {
            return mejorMatch.get().normalizar(); // Devuelve el string del termino, o si es sinonimo, al termino que apunta
        }
    }

    // Agrega un termino como admitido por el normalizador
    public void agregarTermino(String nombre) {
        terminosConocidos.add(new Termino(nombre));
    }

    // Agrega un termino como sinonimo de otro. Si el primero no existe como termino, tira NoSuchElementException
    public void agregarSinonimo(String terminoRaiz, String sinonimo) throws NoSuchElementException {
        terminosConocidos.add(new Termino(sinonimo, terminosConocidos.stream().filter(t -> t.coincide(terminoRaiz)).findFirst().get()));
    }

    // Aplica el algoritmo de hallar el mejor match
    private Optional<Termino> hallarMejorMatch(String terminoAComparar, Integer umbralDeNormalizacion) {
        Termino mejor = null;
        int mejorDistancia = Integer.MAX_VALUE;
        for (Termino t : terminosConocidos) {
            Integer distancia = t.distanciaLevenshtein(terminoAComparar, umbralDeNormalizacion);
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

