package aplicacion.services;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.clasesIntermedias.HechoXColeccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Etiqueta;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.excepciones.CategoriaYaPresenteException;
import aplicacion.excepciones.ContribuyenteNoConfiguradoException;
import aplicacion.excepciones.EtiquetaNoEncontradaException;
import aplicacion.graphql.objects.HechoFiltros;
import aplicacion.graphql.objects.HechoMapItem;
import aplicacion.repositorios.RepositorioDeHechos;
import aplicacion.repositorios.RepositorioDeHechosXColeccion;
import aplicacion.excepciones.HechoNoEncontradoException;
import aplicacion.services.normalizador.NormalizadorDeHechos;
import aplicacion.utils.Md5Hasher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class HechoService {
    private final RepositorioDeHechos repositorioDeHechos;
    private final RepositorioDeHechosXColeccion repositorioDeHechosXColeccion;
    private final HechoOutputMapper hechoOutputMapper;
    private final NormalizadorDeHechos normalizadorDeHechos;
    private final HechoInputMapper hechoInputMapper;
    private final ContribuyenteService contribuyenteService;
    private final EtiquetaService etiquetaService;
    public HechoService(RepositorioDeHechos repositorioDeHechos, RepositorioDeHechosXColeccion repositorioDeHechosXColeccion, HechoOutputMapper hechoOutputMapper, NormalizadorDeHechos normalizadorDeHechos, HechoInputMapper hechoInputMapper, ContribuyenteService contribuyenteService, EtiquetaService etiquetaService) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.repositorioDeHechosXColeccion = repositorioDeHechosXColeccion;
        this.hechoOutputMapper = hechoOutputMapper;
        this.normalizadorDeHechos = normalizadorDeHechos;
        this.hechoInputMapper = hechoInputMapper;
        this.contribuyenteService = contribuyenteService;
        this.etiquetaService = etiquetaService;
    }

    public void guardarHechos(List<Hecho> hechos) {
        for (Hecho hecho : hechos) {
            guardarOActualizarContribuyenteDeHecho(hecho);
        }
        repositorioDeHechos.saveAll(hechos);
    }

    public List<Hecho> obtenerHechos() {
        return repositorioDeHechos.findAll();
    }

    public List<HechoOutputDto> obtenerHechosSinPaginar() {
        return obtenerHechos().stream().map(hechoOutputMapper::map).toList();
    }

    public Page<HechoOutputDto> obtenerHechosAsDto(String categoria,
                                                   LocalDateTime fechaReporteDesde,
                                                   LocalDateTime fechaReporteHasta,
                                                   LocalDateTime fechaAcontecimientoDesde,
                                                   LocalDateTime fechaAcontecimientoHasta,
                                                   Double latitud,
                                                   Double longitud,
                                                   String textoLibre,
                                                   Pageable pageable) {
        // Caso 1: sin texto libre → búsqueda normal
        if (textoLibre == null || textoLibre.trim().isEmpty()) {
            return repositorioDeHechos
                    .filtrarHechos(categoria, fechaReporteDesde, fechaReporteHasta,
                            fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                            latitud, longitud, pageable)
                    .map(hechoOutputMapper::map);
        }

        // Caso 2: buscar primero coincidencia EXACTA
        Page<Hecho> exactos = repositorioDeHechos.buscarPorTituloExacto(
                textoLibre.trim(),
                categoria,
                fechaReporteDesde,
                fechaReporteHasta,
                fechaAcontecimientoDesde,
                fechaAcontecimientoHasta,
                latitud,
                longitud,
                pageable
        );

        if (!exactos.isEmpty()) {
            // Si hay exact matches → devolvemos SOLO esos
            return exactos.map(hechoOutputMapper::map);
        }

        // Caso 3: si no hubo coincidencia exacta → full text
        return repositorioDeHechos
                .filtrarHechosPorTextoLibre(categoria, fechaReporteDesde, fechaReporteHasta,
                        fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                        latitud, longitud, textoLibre.trim(), pageable)
                .map(hechoOutputMapper::map);
    }

    public Page<Hecho> obtenerHechosPorTextoLibre(String textoLibre, Pageable pageable) {
        return repositorioDeHechos.findByTextoLibre(textoLibre, pageable);
    }

    public void guardarHechoPorColeccion(HechoXColeccion hechoPorColeccion) {
        Hecho hecho = hechoPorColeccion.getHecho();
        Coleccion coleccion = hechoPorColeccion.getColeccion();
        // Si el hecho cumple los criterios de pertenencia de la colección, entonces se guarda, en caso contrario no
        if (coleccion.cumpleCriterios(hecho)) {
            repositorioDeHechosXColeccion.save(hechoPorColeccion);
        }
    }

    @Transactional
    public List<HechoOutputDto> obtenerHechosDeContribuyente( Long contribuyenteId ) throws ContribuyenteNoConfiguradoException {
        contribuyenteService.obtenerContribuyente(contribuyenteId);
        return repositorioDeHechos.findByAutorId(contribuyenteId).stream().map(hechoOutputMapper::map).toList();
    }

    public Hecho obtenerHechoPorId(String idHecho)  throws HechoNoEncontradoException{
        return repositorioDeHechos.findById(idHecho).orElseThrow(() -> new HechoNoEncontradoException("No se encontro el hecho con id: " + idHecho));
    }

    public HechoOutputDto obtenerHechoDto(String idHecho) throws HechoNoEncontradoException {
        Hecho hecho = obtenerHechoPorId(idHecho);
        return hechoOutputMapper.map(hecho);
    }

    public Page<Hecho> obtenerHechosIrrestrictosPorColeccion(String idColeccion,
                                                             String categoria,
                                                             LocalDateTime fechaReporteDesde,
                                                             LocalDateTime fechaReporteHasta,
                                                             LocalDateTime fechaAcontecimientoDesde,
                                                             LocalDateTime fechaAcontecimientoHasta,
                                                             Double latitud,
                                                             Double longitud,
                                                             String textoLibre,
                                                             Pageable pageable) {
        if (textoLibre == null || textoLibre.trim().isEmpty()) {
            return repositorioDeHechos.findByFiltrosYColeccionIrrestrictos(
                    idColeccion, categoria, fechaReporteDesde, fechaReporteHasta,
                    fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, pageable
            );
        }

        // 1. Buscar exact match
        Page<Hecho> exactos = repositorioDeHechos.findByFiltrosYColeccionIrrestrictos_TituloExacto(
                idColeccion, textoLibre.trim(), categoria,
                fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                latitud, longitud, pageable
        );

        if (!exactos.isEmpty()) {
            return exactos;
        }

        // 2. Buscar full-text
        return repositorioDeHechos.findByFiltrosYColeccionIrrestrictosYTextoLibre(
                idColeccion, categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                latitud, longitud, textoLibre.trim(), pageable
        );
    }

    public Page<Hecho> obtenerHechosCuradosPorColeccion(String idColeccion,
                                                        String categoria,
                                                        LocalDateTime fechaReporteDesde,
                                                        LocalDateTime fechaReporteHasta,
                                                        LocalDateTime fechaAcontecimientoDesde,
                                                        LocalDateTime fechaAcontecimientoHasta,
                                                        Double latitud,
                                                        Double longitud,
                                                        String textoLibre,
                                                        Pageable pageable) {
        if (textoLibre == null || textoLibre.trim().isEmpty()) {
            return repositorioDeHechos.findByFiltrosYColeccionCurados(
                    idColeccion, categoria, fechaReporteDesde, fechaReporteHasta,
                    fechaAcontecimientoDesde, fechaAcontecimientoHasta, latitud, longitud, pageable
            );
        }

        // 1. Exact match
        Page<Hecho> exactos = repositorioDeHechos.findByFiltrosYColeccionCurados_TituloExacto(
                idColeccion, textoLibre.trim(), categoria,
                fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                latitud, longitud, pageable
        );

        if (!exactos.isEmpty()) {
            return exactos;
        }

        // 2. Full text
        return repositorioDeHechos.findByFiltrosYColeccionCuradosYTextoLibre(
                idColeccion, categoria, fechaReporteDesde, fechaReporteHasta,
                fechaAcontecimientoDesde, fechaAcontecimientoHasta,
                latitud, longitud, textoLibre.trim(), pageable
        );
    }

    public void guardarHecho(Hecho hecho) {
        guardarOActualizarContribuyenteDeHecho(hecho);
        repositorioDeHechos.save(hecho);
    }

    private void guardarOActualizarContribuyenteDeHecho(Hecho hecho) {
        if (hecho.getAutor() != null) {
            Contribuyente contribuyente = contribuyenteService.guardarOActualizar(hecho.getAutor());
            hecho.setAutor(contribuyente);
        }
    }

    public HechoOutputDto agregarHecho(HechoInputDto hechoInputDTO) {
        Hecho hecho = hechoInputMapper.map(hechoInputDTO);
        normalizadorDeHechos.normalizar(hecho);
        hecho = repositorioDeHechos.save(hecho);
        return hechoOutputMapper.map(hecho);
    }

    public void borrarHechosPorColeccion(Coleccion coleccion) {
        repositorioDeHechosXColeccion.deleteAllByColeccionId(coleccion.getId());
    }

    public void quitarHechosDeSublista(List<Hecho> listaOriginal, List<Hecho> hechosAQuitar){
        listaOriginal.removeIf(hechoA -> hechosAQuitar.stream().anyMatch(hechoB -> hechoA == hechoB));
    }

    public List<Hecho> hallarHechosDuplicadosDeBD(List<Hecho> hechosAEvaluar){
        Md5Hasher hasher = Md5Hasher.getInstance();
        List<String> codigosUnicos = hechosAEvaluar.stream().map(Hecho::getClaveUnica).map(hasher::hash).toList();
        return repositorioDeHechos.findByCodigoHasheadoIn(codigosUnicos);
    }

    public Map<Hecho, Long> contarHechosPorFuente(Coleccion coleccion) {
        List<Fuente> fuentes = coleccion.getFuentes();
        List<Hecho> hechos = fuentes.stream().flatMap(fuente -> fuente.getHechos().stream()).toList();
        Set<Hecho> hechosUnicos = new HashSet<>(hechos);
        Map<Hecho, Long> returnMap = new HashMap<>();
        for(Hecho hechoUnico : hechosUnicos){
            Long ocurrencias = 0L;
            for(Fuente fuente : fuentes){
                if(fuente.getHechos().contains(hechoUnico))
                    ocurrencias++;
            }
            returnMap.put(hechoUnico, ocurrencias);
        }
        return returnMap;
    }

    public List<Hecho> hallarHechosDuplicadosDeLista(List<Hecho> hechosAEvaluar){
        List<Hecho> hechosDuplicados = new ArrayList<>();
        Set<String> vistos = new HashSet<>();

        for (Hecho hecho : hechosAEvaluar) {
            if (!vistos.add(hecho.getClaveUnica())) {
                hechosDuplicados.add(hecho);
            }
        }
        return hechosDuplicados;
    }
    @Transactional
    public Etiqueta agregarEtiqueta(String hechoId, String etiquetaName) throws HechoNoEncontradoException {
        Etiqueta etiqueta;
        Optional<Hecho> hecho = repositorioDeHechos.findById(hechoId);
        if(hecho.isEmpty())
            throw new HechoNoEncontradoException("Hecho " + hechoId + " no encontrado");
        etiquetaName = normalizadorDeHechos.normalizarEtiqueta(etiquetaName);
        String finalEtiquetaName = etiquetaName;
        if(hecho.get().getEtiquetas().stream().anyMatch(etiqueta1 -> Objects.equals(etiqueta1.getNombre(), finalEtiquetaName))){
            throw new CategoriaYaPresenteException(hecho.get(), etiquetaName);
        }
        try {
            etiqueta = etiquetaService.obtenerEtiquetaPorNombre(etiquetaName);
        } catch (EtiquetaNoEncontradaException e) {
            etiqueta = etiquetaService.agregarEtiqueta(etiquetaName);
        }
        hecho.get().getEtiquetas().add(etiqueta);
        repositorioDeHechos.save(hecho.get());
        return etiqueta;
    }

    public HechoOutputDto eliminarEtiqueta(String hechoId, String etiquetaName) throws HechoNoEncontradoException, EtiquetaNoEncontradaException {
        Optional<Hecho> hecho = repositorioDeHechos.findById(hechoId);
        if(hecho.isEmpty())
            throw new HechoNoEncontradoException("Hecho " + hechoId + " no encontrado");
        List<Etiqueta> etiquetas = hecho.get().getEtiquetas();
        if(etiquetas.stream().anyMatch(etiqueta -> Objects.equals(etiqueta.getNombre(), etiquetaName))) {
            etiquetas.removeIf(etiqueta -> Objects.equals(etiqueta.getNombre(), etiquetaName));
            repositorioDeHechos.save(hecho.get());
            return hechoOutputMapper.map(hecho.get());
        }
        throw new EtiquetaNoEncontradaException("No se encontró la etiqueta" + etiquetaName);
    }

    public List<String> obtenerAutocompletado(String currentSearch, Integer limit) {
        List<String> opciones = currentSearch.length() >= 3 ? repositorioDeHechos.findAutocompletado(currentSearch, limit) : repositorioDeHechos.findAutocompletadoLike(currentSearch, limit);
        if(opciones.isEmpty() && currentSearch.length() >=3){
            return repositorioDeHechos.findAutocompletadoLike(currentSearch, limit);
        }
        else return opciones;
    }

    public Page<HechoMapItem> obtenerHechosParaMapaGraphql(HechoFiltros filtros, Integer page, Integer limit) {
        Pageable pageable = Pageable.ofSize(limit).withPage(page);
        Page<HechoOutputDto> hechosPage;
        if (filtros == null) {
             hechosPage = this.obtenerHechosAsDto(null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    pageable);
        } else {
            hechosPage = this.obtenerHechosAsDto(filtros.categoria() != null ? filtros.categoria() : null,
                    filtros.fechaReporteDesde() != null ? filtros.fechaReporteDesde().toLocalDateTime() : null,
                    filtros.fechaReporteHasta() != null ? filtros.fechaReporteHasta().toLocalDateTime() : null,
                    filtros.fechaAcontecimientoDesde() != null ? filtros.fechaAcontecimientoDesde().toLocalDateTime() : null,
                    filtros.fechaAcontecimientoHasta() != null ? filtros.fechaAcontecimientoHasta().toLocalDateTime(): null,
                    filtros.latitud()!= null ? filtros.latitud() : null,
                    filtros.longitud() != null ? filtros.longitud() : null,
                    filtros.search() != null  ? filtros.search() : null,
                    pageable);
        }

        return hechosPage.map(hecho -> new HechoMapItem(
                hecho.getId(),
                hecho.getTitulo(),
                hecho.getUbicacion().getLatitud(),
                hecho.getUbicacion().getLongitud(),
                hecho.getCategoria().getNombre(),
                hecho.getFechaCarga() != null ? hecho.getFechaCarga().atOffset(java.time.ZoneOffset.UTC) : null
        ));
    }

    public Page<HechoMapItem> obtenerHechosDeColeccionIrrestrictosGraphql(String idColeccion, HechoFiltros filtros, Integer page, Integer limit ){
        Pageable pageable = Pageable.ofSize(limit).withPage(page);
        Page<Hecho> hechosPage;
        if (filtros == null) {
            hechosPage = this.obtenerHechosIrrestrictosPorColeccion(idColeccion,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    pageable);
        } else {
            hechosPage = this.obtenerHechosIrrestrictosPorColeccion(idColeccion,
                    filtros.categoria() != null ? filtros.categoria() : null,
                    filtros.fechaReporteDesde() != null ? filtros.fechaReporteDesde().toLocalDateTime() : null,
                    filtros.fechaReporteHasta() != null ? filtros.fechaReporteHasta().toLocalDateTime() : null,
                    filtros.fechaAcontecimientoDesde() != null ? filtros.fechaAcontecimientoDesde().toLocalDateTime() : null,
                    filtros.fechaAcontecimientoHasta() != null ? filtros.fechaAcontecimientoHasta().toLocalDateTime(): null,
                    filtros.latitud()!= null ? filtros.latitud() : null,
                    filtros.longitud() != null ? filtros.longitud() : null,
                    filtros.search() != null ? filtros.search() : null,
                    pageable);
        }

        return hechosPage.map(hecho -> new HechoMapItem(
                hecho.getId(),
                hecho.getTitulo(),
                hecho.getUbicacion().getLatitud(),
                hecho.getUbicacion().getLongitud(),
                hecho.getCategoria().getNombre(),
                hecho.getFechaCarga() != null ? hecho.getFechaCarga().atOffset(java.time.ZoneOffset.UTC) : null
        ));
    }

    public Page<HechoMapItem> obtenerHechosDeColeccionCuradosGraphql(String idColeccion, HechoFiltros filtros, Integer page, Integer limit){
        Pageable pageable = Pageable.ofSize(limit).withPage(page);
        Page<Hecho> hechosPage;
        if (filtros == null) {
            hechosPage = this.obtenerHechosCuradosPorColeccion(idColeccion,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    pageable);
        } else {
            hechosPage = this.obtenerHechosCuradosPorColeccion(idColeccion,
                    filtros.categoria() != null ? filtros.categoria() : null,
                    filtros.fechaReporteDesde() != null ? filtros.fechaReporteDesde().toLocalDateTime() : null,
                    filtros.fechaReporteHasta() != null ? filtros.fechaReporteHasta().toLocalDateTime() : null,
                    filtros.fechaAcontecimientoDesde() != null ? filtros.fechaAcontecimientoDesde().toLocalDateTime() : null,
                    filtros.fechaAcontecimientoHasta() != null ? filtros.fechaAcontecimientoHasta().toLocalDateTime(): null,
                    filtros.latitud()!= null ? filtros.latitud() : null,
                    filtros.longitud() != null ? filtros.longitud() : null,
                    filtros.search() != null ? filtros.search() : null,
                    pageable);
        }

        return hechosPage.map(hecho -> new HechoMapItem(
                hecho.getId(),
                hecho.getTitulo(),
                hecho.getUbicacion().getLatitud(),
                hecho.getUbicacion().getLongitud(),
                hecho.getCategoria().getNombre(),
                hecho.getFechaCarga() != null ? hecho.getFechaCarga().atOffset(java.time.ZoneOffset.UTC) : null
        ));
    }
}