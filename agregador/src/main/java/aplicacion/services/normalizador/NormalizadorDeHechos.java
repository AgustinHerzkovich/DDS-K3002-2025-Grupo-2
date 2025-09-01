package aplicacion.services.normalizador;

import aplicacion.excepciones.UbicacionNoEncontradaException;
import aplicacion.services.CategoriaService;
import aplicacion.services.EtiquetaService;
import aplicacion.excepciones.CategoriaNoEncontradaException;
import aplicacion.excepciones.EtiquetaNoEncontradaException;
import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Etiqueta;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.UbicacionService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NormalizadorDeHechos {
    private final NormalizadorDeTerminos normalizadorDeCategorias;
    private final NormalizadorDeTerminos normalizadorDeEtiquetas;
    private final CategoriaService categoriaService;
    private final EtiquetaService etiquetaService;
    private final UbicacionService ubicacionService;

    public NormalizadorDeHechos(CategoriaService categoriaService, EtiquetaService etiquetaService, UbicacionService ubicacionService) {
        Integer umbralLevenshtein = 3;
        normalizadorDeEtiquetas = new NormalizadorDeTerminos(umbralLevenshtein);
        normalizadorDeCategorias = new NormalizadorDeTerminos(umbralLevenshtein);
        this.etiquetaService = etiquetaService;
        this.categoriaService = categoriaService;
        this.ubicacionService = ubicacionService;
    }

    public void normalizar(Hecho hecho)  {
        Categoria categoriaAInyectar;
        List<Etiqueta> etiquetasAInyectar = new ArrayList<>();
        String categoria = aplicarNormalizador(hecho.getCategoria().getNombre(), normalizadorDeCategorias);
        try{
            categoriaAInyectar = categoriaService.obtenerCategoriaPorNombre(categoria);
        }catch (CategoriaNoEncontradaException e){
            categoriaAInyectar = categoriaService.agregarCategoria(categoria);
        }
        hecho.setCategoria(categoriaAInyectar);

        List<String> etiquetas = hecho.getEtiquetas().stream().map(Etiqueta::getNombre).map(n->aplicarNormalizador(n, normalizadorDeEtiquetas)).toList();
        Etiqueta etiquetaAInyectar;
        for(String nombre : etiquetas){
            try{
                etiquetaAInyectar = etiquetaService.obtenerEtiquetaPorNombre(nombre);
            } catch (EtiquetaNoEncontradaException e){
                etiquetaAInyectar = etiquetaService.agregarEtiqueta(nombre);
            }
            etiquetasAInyectar.add(etiquetaAInyectar);
        }
        hecho.setEtiquetas(etiquetasAInyectar);

        Ubicacion ubicacionAInyectar = normalizarUbicacion(hecho.getUbicacion());
        hecho.setUbicacion(ubicacionAInyectar);
    }

    public String normalizarCategoria(String categoria) {
        return aplicarNormalizador(categoria, normalizadorDeCategorias);
    }

    public Ubicacion normalizarUbicacion(Ubicacion ubicacion) {
        Ubicacion ubicacionAInyectar;
        try {
            ubicacionAInyectar = ubicacionService.obtenerUbicacionPorLatitudYLongitud(ubicacion.getLatitud(), ubicacion.getLongitud());
        } catch (UbicacionNoEncontradaException e) {
            ubicacionAInyectar = ubicacionService.agregarUbicacion(ubicacion.getLatitud(), ubicacion.getLongitud());
        }
        return ubicacionAInyectar;
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
            normalizador.agregarTermino(termino);
            return termino;
        }
    }
}
