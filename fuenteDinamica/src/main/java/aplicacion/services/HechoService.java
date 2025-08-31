package aplicacion.services;

import aplicacion.domain.usuarios.IdentidadContribuyente;
import aplicacion.dto.input.CambioEstadoRevisionInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.input.HechoEdicionInputDto;
import aplicacion.domain.hechos.EstadoRevision;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.mappers.HechoOutputMapper;
import aplicacion.dto.mappers.HechoRevisadoOutputMapper;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.dto.output.HechoRevisadoOutputDto;
import aplicacion.excepciones.*;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.repositorios.RepositorioDeHechos;
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

    
    public HechoService(RepositorioDeHechos repositorioDeHechos, ContribuyenteService contribuyenteService, HechoInputMapper hechoInputMapper, HechoOutputMapper hechoOutputMapper, HechoRevisadoOutputMapper hechoRevisadoOutputMapper) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.contribuyenteService = contribuyenteService;
        this.hechoInputMapper = hechoInputMapper;
        this.hechoOutputMapper = hechoOutputMapper;
        this.hechoRevisadoOutputMapper = hechoRevisadoOutputMapper;
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
    public List<HechoOutputDto> obtenerHechosAceptadosConFechaMayorA(LocalDateTime fechaMayorA) {
        return this.obtenerHechosAceptados().stream().filter(hecho -> hecho.getFechaUltimaModificacion().isAfter(fechaMayorA)).toList();
    }

    @Transactional(readOnly = true)
    public HechoOutputDto guardarHecho(HechoInputDto hechoInputDto) throws ContribuyenteNoConfiguradoException {
        Long identidadId = hechoInputDto.getIdentidadId();
        IdentidadContribuyente autor = null;
        if (!hechoInputDto.getAnonimato() && identidadId == null) {
            throw new ContribuyenteNoConfiguradoException("El contribuyente debe estar configurado si no se carga el hecho en anonimato.");
        }
        if (!hechoInputDto.getAnonimato()) {
            autor = contribuyenteService.obtenerIdentidad(hechoInputDto.getIdentidadId());
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

    public HechoOutputDto editarHecho(String id, HechoEdicionInputDto hechoEdicionInputDto) throws HechoNoEncontradoException, PlazoEdicionVencidoException, AnonimatoException {
        Hecho hecho = repositorioDeHechos.findById(id)
                    .orElseThrow(() -> new HechoNoEncontradoException("Hecho no encontrado con ID: " + id));
        this.validarHechoEditable(hecho);

        hecho.editar(hechoEdicionInputDto.getTitulo(),
                hechoEdicionInputDto.getDescripcion(),
                hechoEdicionInputDto.getCategoria(),
                hechoEdicionInputDto.getUbicacion(),
                hechoEdicionInputDto.getFechaAcontecimiento(),
                hechoEdicionInputDto.getContenidoTexto(),
                hechoEdicionInputDto.getContenidoMultimedia());

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
