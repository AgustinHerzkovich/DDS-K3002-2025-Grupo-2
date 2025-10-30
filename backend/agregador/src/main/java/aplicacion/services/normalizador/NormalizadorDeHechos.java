package aplicacion.services.normalizador;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.services.CategoriaService;
import aplicacion.services.EtiquetaService;
import aplicacion.excepciones.CategoriaNoEncontradaException;
import aplicacion.excepciones.EtiquetaNoEncontradaException;
import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Etiqueta;
import aplicacion.domain.hechos.Hecho;
import aplicacion.utils.ProgressBar;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class NormalizadorDeHechos {
    private final NormalizadorDeTerminos normalizadorDeCategorias;
    private final NormalizadorDeTerminos normalizadorDeEtiquetas;
    private final CategoriaService categoriaService;
    private final EtiquetaService etiquetaService;
    Cache<String, Categoria> categoriaCache = Caffeine.newBuilder().maximumSize(5000).expireAfterWrite(10, TimeUnit.MINUTES).build();
    Cache<String, Etiqueta> etiquetaCache = Caffeine.newBuilder().maximumSize(10000).expireAfterWrite(10, TimeUnit.MINUTES).build();

    public NormalizadorDeHechos(CategoriaService categoriaService, EtiquetaService etiquetaService) {
        Integer umbralLevenshtein = 1;
        normalizadorDeEtiquetas = new NormalizadorDeTerminos(umbralLevenshtein);
        normalizadorDeCategorias = new NormalizadorDeTerminos(umbralLevenshtein);
        this.etiquetaService = etiquetaService;
        this.categoriaService = categoriaService;
    }

    public void normalizarTodos(Map<Fuente, List<Hecho>> mapFuentesYhechosANormalizar){
        List<Long> tiempoPorPaso = new ArrayList<>();
        tiempoPorPaso.add(0L); // Categorias
        tiempoPorPaso.add(0L); // Etiquetas
        List<Hecho> hechos = mapFuentesYhechosANormalizar.values().stream()
                .flatMap(List::stream)
                .toList();
        System.out.println("Hechos a normalizar: " + mapFuentesYhechosANormalizar.values().stream().mapToInt(List::size).sum());
        System.out.println("Normalizando...");
        ProgressBar progressBar = new ProgressBar(hechos.size());
        for(Hecho hecho : hechos){
            normalizarCronometrado(hecho, tiempoPorPaso);
            progressBar.avanzar();
        }
        Long tiempoCategorias = tiempoPorPaso.get(0);
        Long tiempoEtiquetas = tiempoPorPaso.get(1);
        Long tiempoTotal = tiempoCategorias + tiempoEtiquetas;
        System.out.println("\nNormalización finalizada.\n");
        if(tiempoTotal != 0) {
            System.out.println("          " + tiempoPorPaso.get(0) / 1_000_000 + " ms en normalizar Categorias" + "(" + (double) tiempoCategorias / tiempoTotal * 100 + " %)");
            System.out.println("          " + tiempoPorPaso.get(1) / 1_000_000 + " ms en normalizar Etiquetas" + "(" + (double) tiempoEtiquetas / tiempoTotal * 100 + " %)");
        }
    }
    public void normalizarMultiThread(Map<Fuente, List<Hecho>> mapFuentesYhechosANormalizar){
        ExecutorService executor = Executors.newFixedThreadPool(3);
        final ConcurrentHashMap<String, Object> locks =  new ConcurrentHashMap<>();
        Object lockEtiqueta = locks.computeIfAbsent("etiqueta", k -> new Object());
        Object lockCategoria = locks.computeIfAbsent("categoria", k -> new Object());
        for(Hecho hecho : mapFuentesYhechosANormalizar.values().stream().flatMap(List::stream).toList())
           executor.submit(() -> normalizarsincronizado(hecho, lockEtiqueta, lockCategoria));
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) { // Si no termina en una hora, fuerzo que se detenga
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {  // No debería pasar, pero por las dudas
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void normalizarsincronizado(Hecho hecho, Object lockEtiqueta, Object lockCategoria){
        Categoria categoriaAInyectar;
        List<Etiqueta> etiquetasAInyectar = new ArrayList<>();
        synchronized (lockCategoria) {
            String categoria = aplicarNormalizador(hecho.getCategoria().getNombre(), normalizadorDeCategorias);
            try {
                categoriaAInyectar = categoriaService.obtenerCategoriaPorNombre(categoria);
            } catch (CategoriaNoEncontradaException e) {
                categoriaAInyectar = categoriaService.agregarCategoria(categoria);
            }
        }
        hecho.setCategoria(categoriaAInyectar);
        Etiqueta etiquetaAInyectar;
        synchronized (lockEtiqueta){
            List<String> etiquetas = hecho.getEtiquetas().stream().map(Etiqueta::getNombre).map(n->aplicarNormalizador(n, normalizadorDeEtiquetas)).toList();
            for(String nombre : etiquetas){
                try{
                    etiquetaAInyectar = etiquetaService.obtenerEtiquetaPorNombre(nombre);
                } catch (EtiquetaNoEncontradaException e){
                    etiquetaAInyectar = etiquetaService.agregarEtiqueta(nombre);
                }
                etiquetasAInyectar.add(etiquetaAInyectar);
            }
        }
        hecho.setEtiquetas(etiquetasAInyectar);
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
    }

    public void normalizarCronometrado(Hecho hecho, List<Long> segundosPorPaso)  {
        Categoria categoriaAInyectar;
        List<Etiqueta> etiquetasAInyectar = new ArrayList<>();
        long inicioCat = System.nanoTime();
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
        long finCat = System.nanoTime();

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
        long finEt = System.nanoTime();
        segundosPorPaso.set(0, segundosPorPaso.get(0) + finCat - inicioCat);
        segundosPorPaso.set(1, segundosPorPaso.get(1) + finEt - finCat);
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
