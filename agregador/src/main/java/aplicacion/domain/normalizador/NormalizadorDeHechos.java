package aplicacion.domain.normalizador;

import domain.hechos.Categoria;
import domain.hechos.Etiqueta;
import domain.hechos.Ubicacion;
import domain.normalizadorDeTerminos.NingunTerminoCumpleUmbralException;
import domain.normalizadorDeTerminos.NormalizadorDeTerminos;
import domain.hechos.Hecho;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class NormalizadorDeHechos {
    public NormalizadorDeTerminos normalizadorDeCategorias;
    public NormalizadorDeTerminos normalizadorDeEtiquetas;

    public NormalizadorDeHechos() {
        Integer umbralLevenshtein = 3;
        normalizadorDeEtiquetas = new NormalizadorDeTerminos(umbralLevenshtein);
        normalizadorDeCategorias = new NormalizadorDeTerminos(umbralLevenshtein);
    }

    public Hecho normalizar(Hecho hecho) {
        hecho.setCategoria(new Categoria(normalizadorDeCategorias.normalizarTermino(hecho.getCategoria().getNombre())));
        hecho.setEtiquetas(new ArrayList<Etiqueta>(hecho.getEtiquetas().stream().map(Etiqueta::getNombre).map(n->new Etiqueta(normalizadorDeEtiquetas.normalizarTermino(n))).toList()));
        return hecho;
    }

    public String normalizarCategoria(String categoria) {
        return aplicarNormalizador(categoria, normalizadorDeCategorias);
    }
    public Ubicacion normalizarUbicacion(Ubicacion ubicacion) {
        return new Ubicacion(); // TODO
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
