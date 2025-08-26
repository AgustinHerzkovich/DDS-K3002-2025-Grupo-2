package domain.services;

import domain.dto.HechoDTO;
import domain.dto.HechoEdicionDTO;
import domain.excepciones.AnonimatoException;
import domain.excepciones.HechoNoEncontradoException;
import domain.excepciones.PlazoEdicionVencidoException;
import domain.hechos.EstadoRevision;
import domain.hechos.Hecho;
import domain.mappers.HechoMapper;
import domain.repositorios.RepositorioDeHechos;
import domain.usuarios.Contribuyente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HechoService {
    private final RepositorioDeHechos repositorioDeHechos;
    private final ContribuyenteService contribuyenteService;
    
    public HechoService(RepositorioDeHechos repositorioDeHechos, ContribuyenteService contribuyenteService) {
        this.repositorioDeHechos = repositorioDeHechos;
        this.contribuyenteService = contribuyenteService;
    }

    @Transactional(readOnly = true) // Asegura que la sesión esté abierta cuando se haga la serialización
    public List<Hecho> obtenerHechos() {
        return repositorioDeHechos.findAll();
    }

    @Transactional(readOnly = true)
    public List<Hecho> obtenerHechosAceptados() {
        return this.obtenerHechos().stream().filter(hecho -> hecho.getEstadoRevision() == EstadoRevision.ACEPTADO).toList();
    }

    @Transactional(readOnly = true)
    public List<Hecho> obtenerHechosAceptadosConFechaMayorA(LocalDateTime fechaMayorA) {
        return this.obtenerHechosAceptados().stream().filter(hecho -> hecho.getFechaUltimaModificacion().isAfter(fechaMayorA)).toList();
    }

    public Hecho guardarHecho(Hecho hecho) {
        return repositorioDeHechos.save(hecho);
    }

    @Transactional(readOnly = true)
    public Hecho guardarHechoDto(HechoDTO hechoDto) {
        Contribuyente autor = contribuyenteService.obtenerContribuyente(hechoDto.getContribuyenteId());
        Hecho hecho = new HechoMapper().map(hechoDto, autor.getUltimaIdentidad()); // TODO: Actualmente se fuerza que use la última identidad, pero debería ser la identidad seleccionada en el front
        autor.contribuirAlHecho(hecho);
        return guardarHecho(hecho);
    }

    public Hecho obtenerHecho(String id) throws HechoNoEncontradoException {
        return repositorioDeHechos.findById(id)
                .orElseThrow(() -> new HechoNoEncontradoException("Hecho no encontrado con ID: " + id));
    }

    public Hecho modificarEstadoRevision(String id, EstadoRevision nuevoEstado) throws HechoNoEncontradoException {
        Hecho hecho = repositorioDeHechos.findById(id)
                .orElseThrow(() -> new HechoNoEncontradoException("Hecho no encontrado con ID: " + id));
        hecho.setEstadoRevision(nuevoEstado);
        return repositorioDeHechos.save(hecho);
    }

    public Hecho editarHecho(String id, HechoEdicionDTO hechoEdicionDto) throws HechoNoEncontradoException
    , PlazoEdicionVencidoException, AnonimatoException {
            Hecho hecho = repositorioDeHechos.findById(id)
                    .orElseThrow(() -> new HechoNoEncontradoException("Hecho no encontrado con ID: " + id));
        this.validarHechoEditable(hecho);
        hecho.editar(hechoEdicionDto.getTitulo(),
                hechoEdicionDto.getDescripcion(),
                hechoEdicionDto.getCategoria(),
                hechoEdicionDto.getUbicacion(),
                hechoEdicionDto.getFechaAcontecimiento(),
                hechoEdicionDto.getContenidoTexto(),
                hechoEdicionDto.getContenidoMultimedia());
        return repositorioDeHechos.save(hecho);
    }

    public void validarHechoEditable(Hecho hecho) throws PlazoEdicionVencidoException, AnonimatoException {
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
