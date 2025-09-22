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
        tiempoPorPaso.add(0L); // Ubicaciones
        List<Hecho> hechos = mapFuentesYhechosANormalizar.values().stream()
                .flatMap(List::stream)
                .toList();
        System.out.println("Hechos a normalizar: " + mapFuentesYhechosANormalizar.values().stream().mapToInt(List::size).sum());
        System.out.println("Normalizando...");
        int largoBarra = 50;
        int totalHechos = hechos.size();
        int hechosNormalizados = 0;
        for(Hecho hecho : hechos){
            normalizarCronometrado(hecho, tiempoPorPaso);
            hechosNormalizados++;

            int porcentaje = (hechosNormalizados * 100) / totalHechos;
            int llenos = (porcentaje * largoBarra) / 100;
            int vacios = largoBarra - llenos;

            String barra = "[" + "#".repeat(llenos) + "-".repeat(vacios) + "] " + porcentaje + "%" + " (" + hechosNormalizados + "/" + totalHechos + ")";
            System.out.print("\r" + barra);
        }
        Long tiempoCategorias = tiempoPorPaso.get(0);
        Long tiempoEtiquetas = tiempoPorPaso.get(1);
        Long tiempoUbicaciones = tiempoPorPaso.get(2);
        Long tiempoTotal = tiempoUbicaciones + tiempoCategorias + tiempoEtiquetas;
        System.out.println("Normalización finalizada.");
        if(tiempoTotal != 0) {
            System.out.println(tiempoPorPaso.get(0) / 1_000_000 + " ms en normalizar Categorias" + "(" + tiempoCategorias / tiempoTotal * 100 + " %)");
            System.out.println(tiempoPorPaso.get(1) / 1_000_000 + " ms en normalizar Etiquetas" + "(" + tiempoEtiquetas / tiempoTotal * 100 + " %)");
            System.out.println(tiempoPorPaso.get(2) / 1_000_000 + " ms en normalizar Ubicaciones" + "(" + tiempoUbicaciones / tiempoTotal * 100 + " %)");
        }
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

    public void normalizarCronometrado(Hecho hecho, List<Long> segundosPorPaso)  {
        Categoria categoriaAInyectar;
        List<Etiqueta> etiquetasAInyectar = new ArrayList<>();
        long inicioCat = System.nanoTime();
        String categoria = aplicarNormalizador(hecho.getCategoria().getNombre(), normalizadorDeCategorias);
        try{
            categoriaAInyectar = categoriaService.obtenerCategoriaPorNombre(categoria);
        }catch (CategoriaNoEncontradaException e){
            categoriaAInyectar = categoriaService.agregarCategoria(categoria);
        }
        hecho.setCategoria(categoriaAInyectar);
        long finCat = System.nanoTime();

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
        long finEt = System.nanoTime();

        Ubicacion ubicacionAInyectar = normalizarUbicacion(hecho.getUbicacion());
        hecho.setUbicacion(ubicacionAInyectar);
        long finUbi = System.nanoTime();
        segundosPorPaso.set(0, segundosPorPaso.get(0) + finCat - inicioCat);
        segundosPorPaso.set(1, segundosPorPaso.get(1) + finEt - finCat);
        segundosPorPaso.set(2, segundosPorPaso.get(2) + finUbi - finEt);
    }
    public String normalizarCategoria(String categoria) {
        return aplicarNormalizador(categoria, normalizadorDeCategorias);
    }

    public Ubicacion normalizarUbicacion(Ubicacion ubicacion) {
        return ubicacion;
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
