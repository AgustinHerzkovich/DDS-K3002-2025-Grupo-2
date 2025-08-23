package domain.normalizador;

import domain.normalizadorDeTerminos.NingunTerminoCumpleUmbralException;
import domain.normalizadorDeTerminos.NormalizadorDeTerminos;
import domain.hechos.Hecho;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class NormalizadorDeHechos {
    @Setter
    @Getter
    public NormalizadorDeTerminos normalizadorDeCategorias;
    public NormalizadorDeTerminos normalizadorDeEtiquetas;

    public NormalizadorDeHechos(Integer umbralLevenshtein) {
        normalizadorDeEtiquetas = new NormalizadorDeTerminos(umbralLevenshtein);
        normalizadorDeCategorias = new NormalizadorDeTerminos(umbralLevenshtein);
    }

    public Hecho normalizar(Hecho hecho) {
        //TODO: facilitar normalizar el hecho entero
        // Sujeto a cambios en cuanto a la entrada
        // Si se admite otros tipos de (por ejemplo) ubicación, sera necesario
        // un hechoDTO que admita ubicación String o algo similar
        return hecho;
    }

    public String normalizarCategoria(String categoria) {
        return aplicarNormalizador(categoria, normalizadorDeCategorias);
    }

    public String normalizarEtiqueta(String etiqueta) {
        return aplicarNormalizador(etiqueta, normalizadorDeEtiquetas);
    }


    public void agregarEtiqueta(String etiqueta) {
        normalizadorDeEtiquetas.agregarTermino(etiqueta);
    }

    public void agregarCategoria(String categoria) {
        normalizadorDeCategorias.agregarTermino(categoria);
    }

    public void agregarSinonimoEtiqueta(String etiquetaRaiz, String etiquetaSinonimo) {
        normalizadorDeEtiquetas.agregarSinonimo(etiquetaRaiz, etiquetaSinonimo);
    }

    public void agregarSinonimoCategoria(String categoriaRaiz, String categoriaSinonimo) {
        normalizadorDeCategorias.agregarSinonimo(categoriaRaiz, categoriaSinonimo);
    }

    private String aplicarNormalizador(String termino, NormalizadorDeTerminos normalizador) {
        try {
            return normalizador.normalizarTermino(termino);
        } catch (NingunTerminoCumpleUmbralException e) {
            // TODO: ver que pasa si no existe la categoria
            normalizador.agregarTermino(termino);
            return termino;
        }
    }
}
