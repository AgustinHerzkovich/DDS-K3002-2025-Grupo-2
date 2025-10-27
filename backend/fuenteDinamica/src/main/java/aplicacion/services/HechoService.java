package aplicacion.services;

import aplicacion.domain.hechos.RevisionHecho;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.CambioEstadoRevisionInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.input.HechoEdicionInputDto;
import aplicacion.domain.hechos.EstadoRevision;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.mappers.*;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.dto.output.HechoRevisadoOutputDto;
import aplicacion.excepciones.*;
import aplicacion.repositorios.RepositorioDeHechos;
import aplicacion.repositorios.RepositorioDeRevisiones;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HechoService {
    private final RepositorioDeHechos repositorioDeHechos;
    private final ContribuyenteService contribuyenteService;
    private final HechoInputMapper hechoInputMapper;
    private final HechoOutputMapper hechoOutputMapper;
    private final HechoRevisadoOutputMapper hechoRevisadoOutputMapper;
    private final CategoriaInputMapper categoriaInputMapper;
    private final UbicacionInputMapper ubicacionInputMapper;
    private final MultimediaInputMapper multimediaInputMapper;
    private final RepositorioDeRevisiones repositorioDeRevisiones;


    public HechoService(RepositorioDeHechos repositorioDeHechos, ContribuyenteService contribuyenteService, HechoInputMapper hechoInputMapper, HechoOutputMapper hechoOutputMapper, HechoRevisadoOutputMapper hechoRevisadoOutputMapper, CategoriaInputMapper categoriaInputMapper, UbicacionInputMapper ubicacionInputMapper, MultimediaInputMapper multimediaInputMapper, RepositorioDeRevisiones repositorioDeRevisiones) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.contribuyenteService = contribuyenteService;
        this.hechoInputMapper = hechoInputMapper;
        this.hechoOutputMapper = hechoOutputMapper;
        this.hechoRevisadoOutputMapper = hechoRevisadoOutputMapper;
        this.categoriaInputMapper = categoriaInputMapper;
        this.ubicacionInputMapper = ubicacionInputMapper;
        this.multimediaInputMapper = multimediaInputMapper;
        this.repositorioDeRevisiones = repositorioDeRevisiones;
    }

    @Transactional
    public List<HechoOutputDto> obtenerHechosDeContribuyente( Long contribuyenteId ) throws ContribuyenteNoConfiguradoException {
        contribuyenteService.obtenerContribuyente(contribuyenteId);
        return repositorioDeHechos.findByAutorId(contribuyenteId).stream().map(hechoOutputMapper::map).toList();
    }

    @Transactional(readOnly = true) // Asegura que la sesión esté abierta cuando se haga la serialización
    public List<Hecho> obtenerHechos() {
        return repositorioDeHechos.findAll();
    }

    @Transactional(readOnly = true)
    public List<HechoOutputDto> obtenerHechosAceptados() {
        return this.obtenerHechos().stream().filter(Hecho::estaAceptado).map(hechoOutputMapper::map).toList();
    }
    @Transactional(readOnly = true)
    public List<HechoOutputDto> obtenerHechosPendientes() {
        return this.obtenerHechos().stream().filter(Hecho::estaPendiente).map(hechoOutputMapper::map).toList();
    }

    @Transactional(readOnly = true)
    public List<HechoOutputDto> obtenerHechosAceptadosConFechaMayorA(LocalDateTime fechaMayorA) {
        return this.obtenerHechosAceptados().stream().filter(hecho -> hecho.getFechaUltimaModificacion().isAfter(fechaMayorA)).toList();
    }

    @Transactional //(readOnly = true)
    public HechoOutputDto guardarHecho(HechoInputDto hechoInputDto) throws ContribuyenteNoConfiguradoException {
        Long identidadId = hechoInputDto.getContribuyenteId();
        Contribuyente autor = null;
        if (!hechoInputDto.getAnonimato() && identidadId == null) {
            throw new ContribuyenteNoConfiguradoException("El contribuyente debe estar configurado si no se carga el hecho en anonimato.");
        }
        if (!hechoInputDto.getAnonimato()) {
            autor = contribuyenteService.obtenerContribuyente(hechoInputDto.getContribuyenteId());
        }
        Hecho hecho = hechoInputMapper.map(hechoInputDto, autor);
        hecho = repositorioDeHechos.save(hecho);
        return hechoOutputMapper.map(hecho);
    }

    public HechoOutputDto obtenerHecho(String id) throws HechoNoEncontradoException {
        Hecho hecho = repositorioDeHechos.findById(id)
                .orElseThrow(() -> new HechoNoEncontradoException("Hecho no encontrado con ID: " + id));
        return hechoOutputMapper.map(hecho);
    }

    public HechoRevisadoOutputDto modificarEstadoRevision(String id, CambioEstadoRevisionInputDto cambioEstadoRevisionInputDto) throws HechoNoEncontradoException {
        Hecho hecho = repositorioDeHechos.findById(id)
                .orElseThrow(() -> new HechoNoEncontradoException("Hecho no encontrado con ID: " + id));
        EstadoRevision estadoRevision = cambioEstadoRevisionInputDto.getEstado();
        String sugerencia = cambioEstadoRevisionInputDto.getSugerencia();
        hecho.setEstadoRevision(estadoRevision);
        if (estadoRevision == EstadoRevision.ACEPTADO && sugerencia != null) {
            hecho.setSugerencia(sugerencia);
        }

        hecho = repositorioDeHechos.save(hecho);
        return hechoRevisadoOutputMapper.map(hecho);
    }

    public void guardarRevision(String hechoId, Long administradorId) throws HechoNoEncontradoException, ContribuyenteNoConfiguradoException {
        Hecho hecho = repositorioDeHechos.findById(hechoId)
                .orElseThrow(() -> new HechoNoEncontradoException("Hecho no encontrado con ID: " + hechoId));
        Contribuyente administrador = contribuyenteService.obtenerContribuyente(administradorId);
        RevisionHecho revisionHecho = new RevisionHecho(administrador, hecho);
        repositorioDeRevisiones.save(revisionHecho);
    }

    public HechoOutputDto editarHecho(String id, HechoEdicionInputDto hechoEdicionInputDto) throws HechoNoEncontradoException, PlazoEdicionVencidoException, AnonimatoException {
        Hecho hecho = repositorioDeHechos.findById(id)
                    .orElseThrow(() -> new HechoNoEncontradoException("Hecho no encontrado con ID: " + id));
        this.validarHechoEditable(hecho);

        hecho.editar(hechoEdicionInputDto.getTitulo(),
                hechoEdicionInputDto.getDescripcion(),
                hechoEdicionInputDto.getCategoria() != null ? categoriaInputMapper.map(hechoEdicionInputDto.getCategoria()) : null,
                hechoEdicionInputDto.getUbicacion() != null ? ubicacionInputMapper.map(hechoEdicionInputDto.getUbicacion()) : null,
                hechoEdicionInputDto.getFechaAcontecimiento(),
                hechoEdicionInputDto.getContenidoTexto(),
                hechoEdicionInputDto.getContenidoMultimedia() != null ? hechoEdicionInputDto.getContenidoMultimedia().stream().map(multimediaInputMapper::map).toList() : null);

        hecho = repositorioDeHechos.save(hecho);
        return hechoOutputMapper.map(hecho);
    }

    private void validarHechoEditable(Hecho hecho) throws PlazoEdicionVencidoException, AnonimatoException {
        LocalDateTime fechaCarga = hecho.getFechaCarga();
        LocalDateTime fechaActual = LocalDateTime.now();
        if (!fechaCarga.isAfter(fechaActual.minusWeeks(1))) { // Si pasó más de una semana desde que se subió, se arroja una excepción
            throw new PlazoEdicionVencidoException();
        }

        if (hecho.getAnonimato()) {
            throw new AnonimatoException();
        }
    }
}
