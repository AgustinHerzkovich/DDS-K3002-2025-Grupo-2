package aplicacion.services.normalizador;

import aplicacion.domain.colecciones.fuentes.Fuente;
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
    private final UbicacionService ubicacionService;

    public NormalizadorDeHechos(CategoriaService categoriaService, EtiquetaService etiquetaService, UbicacionService ubicacionService) {
        Integer umbralLevenshtein = 1;
        normalizadorDeEtiquetas = new NormalizadorDeTerminos(umbralLevenshtein);
        normalizadorDeCategorias = new NormalizadorDeTerminos(umbralLevenshtein);
        this.etiquetaService = etiquetaService;
        this.categoriaService = categoriaService;
        this.ubicacionService = ubicacionService;
    }

    public void normalizarMultiThread(Map<Fuente, List<Hecho>> mapFuentesYhechosANormalizar){
        ExecutorService executor = Executors.newFixedThreadPool(3);
        final ConcurrentHashMap<String, Object> locks =  new ConcurrentHashMap<>();
        Object lockEtiqueta = locks.computeIfAbsent("etiqueta", k -> new Object());
        Object lockCategoria = locks.computeIfAbsent("categoria", k -> new Object());
        Object lockUbicacion = locks.computeIfAbsent("ubicacion", k -> new Object());
        for(Hecho hecho : mapFuentesYhechosANormalizar.values().stream().flatMap(List::stream).toList())
           executor.submit(() -> normalizarsincronizado(hecho, lockEtiqueta, lockCategoria, lockUbicacion));
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

    private void normalizarsincronizado(Hecho hecho, Object lockEtiqueta, Object lockCategoria, Object lockUbicacion){
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
        synchronized (lockUbicacion){
            Ubicacion ubicacionAInyectar = normalizarUbicacion(hecho.getUbicacion());
            hecho.setUbicacion(ubicacionAInyectar);
        }
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
        String terminoNormalizado = normalizador.normalizarTermino(termino);
        if(terminoNormalizado == null) {
            normalizador.agregarTermino(termino);
            terminoNormalizado = termino;
        }
        return terminoNormalizado;
    }
}
