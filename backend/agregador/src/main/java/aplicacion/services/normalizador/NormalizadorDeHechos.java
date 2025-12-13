package aplicacion.services.normalizador;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.services.CategoriaService;
import aplicacion.services.EtiquetaService;
import aplicacion.excepciones.CategoriaNoEncontradaException;
import aplicacion.excepciones.EtiquetaNoEncontradaException;
import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Etiqueta;
import aplicacion.domain.hechos.Hecho;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class NormalizadorDeHechos {
    private final NormalizadorDeTerminos normalizadorDeCategorias;
    private final NormalizadorDeTerminos normalizadorDeEtiquetas;
    private final CategoriaService categoriaService;
    private final EtiquetaService etiquetaService;
    private final Logger logger = LoggerFactory.getLogger(NormalizadorDeHechos.class);
    Cache<String, Categoria> categoriaCache;
    Cache<String, Etiqueta> etiquetaCache;

    public NormalizadorDeHechos(
            @Value("${normalizador.categorias.terminos.max-size:10000}") Integer categoriasMaxSize,
            @Value("${normalizador.categorias.terminos.umbral-levenshtein:1}") Integer categoriasLevenshtein,
            @Value("${normalizador.categorias.cache.max-size:5000}") Integer categoriasCacheSize,
            @Value("${normalizador.etiquetas.terminos.max-size:10000}") Integer etiquetasMaxSize,
            @Value("${normalizador.etiquetas.terminos.umbral-levenshtein:1}") Integer etiquetasLevenshtein,
            @Value("${normalizador.etiquetas.cache.max-size:5000}") Integer etiquetasCacheSize,
            CategoriaService categoriaService,
            EtiquetaService etiquetaService) {
        normalizadorDeEtiquetas = new NormalizadorDeTerminos(etiquetasLevenshtein, etiquetasMaxSize);
        normalizadorDeEtiquetas.agregarTerminos(etiquetaService.obtenerEtiquetas(etiquetasMaxSize));
        normalizadorDeCategorias = new NormalizadorDeTerminos(categoriasLevenshtein, categoriasMaxSize);
        normalizadorDeCategorias.agregarTerminos(categoriaService.obtenerCategorias(categoriasMaxSize));
        this.etiquetaService = etiquetaService;
        this.categoriaService = categoriaService;
        this.etiquetaCache =  Caffeine.newBuilder().maximumSize(etiquetasCacheSize).expireAfterWrite(10, TimeUnit.MINUTES).build();
        this.categoriaCache = Caffeine.newBuilder().maximumSize(categoriasCacheSize).expireAfterWrite(10, TimeUnit.MINUTES).build();
    }

    public void normalizarTodos(Map<Fuente, List<Hecho>> mapFuentesYhechosANormalizar){
        List<Hecho> hechos = mapFuentesYhechosANormalizar.values().stream()
                .flatMap(List::stream)
                .toList();
        logger.debug("Hechos a normalizar: {}", mapFuentesYhechosANormalizar.values().stream().mapToInt(List::size).sum());
        long inicio = System.nanoTime();
        for(Hecho hecho : hechos){
            normalizar(hecho);
        }
        long fin = System.nanoTime();
        long tiempoTotal = fin - inicio;
        logger.debug("Normalización finalizada.{}", tiempoTotal != 0 ? String.format("             %.2fs en normalizar (%.2f hechos/s)%n", (float) tiempoTotal / 1_000_000_000.0, (float) hechos.size() / (tiempoTotal / 1_000_000_000.0)) : "");
    }

    public void normalizar(Hecho hecho)  {
        Categoria categoriaAInyectar;
        List<Etiqueta> etiquetasAInyectar = new ArrayList<>();
        String categoria = aplicarNormalizador(hecho.getCategoria().getNombre(), normalizadorDeCategorias);
        categoriaAInyectar = categoriaCache.getIfPresent(categoria);
        if(categoriaAInyectar == null){
            try{
                categoriaAInyectar = categoriaService.obtenerCategoriaPorNombre(categoria);
            }catch (CategoriaNoEncontradaException e){
                categoriaAInyectar = categoriaService.agregarCategoria(categoria);
            }
            categoriaCache.put(categoria, categoriaAInyectar);
        }
        hecho.setCategoria(categoriaAInyectar);
        List<String> etiquetas = hecho.getEtiquetas().stream().map(Etiqueta::getNombre).map(n->aplicarNormalizador(n, normalizadorDeEtiquetas)).toList();
        Etiqueta etiquetaAInyectar;
        for(String nombre : etiquetas){
            etiquetaAInyectar = etiquetaCache.getIfPresent(nombre);
            if(etiquetaAInyectar == null){
                try{
                    etiquetaAInyectar = etiquetaService.obtenerEtiquetaPorNombre(nombre);
                } catch (EtiquetaNoEncontradaException e){
                    etiquetaAInyectar = etiquetaService.agregarEtiqueta(nombre);
                }
                etiquetaCache.put(nombre, etiquetaAInyectar);
            }
            etiquetasAInyectar.add(etiquetaAInyectar);
        }
        hecho.setEtiquetas(etiquetasAInyectar);
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
        String terminoNormalizado = normalizador.normalizarTermino(termino);
        if(terminoNormalizado == null) {
            normalizador.agregarTermino(termino);
            terminoNormalizado = termino;
        }
        return terminoNormalizado;
    }
}
