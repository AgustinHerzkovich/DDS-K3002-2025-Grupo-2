package aplicacion.services;

import aplicacion.services.dto.HechoDTO;
import aplicacion.services.dto.HechoEdicionDTO;
import aplicacion.domain.hechos.EstadoRevision;
import aplicacion.domain.hechos.Hecho;
import aplicacion.services.excepciones.*;
import aplicacion.services.mappers.HechoMapper;
import aplicacion.repositorios.RepositorioDeHechos;
import aplicacion.domain.usuarios.Contribuyente;
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
    public Hecho guardarHechoDto(HechoDTO hechoDto) throws HechoMappingException, ContribuyenteAssignmentException, HechoStorageException, ContribuyenteNoConfiguradoException {
        Contribuyente autor;
        Hecho hecho;
        try {
            autor = contribuyenteService.obtenerContribuyente(hechoDto.getContribuyenteId());
            assert autor.getUltimaIdentidad() != null;
        }catch (Exception e){
            throw new ContribuyenteNoConfiguradoException("El contribuyente no existe o no tiene identidad. La identidad es necesaria incluso para hechos anonimos");
        }
        try {
            hecho = new HechoMapper().map(hechoDto, autor.getUltimaIdentidad()); // TODO: Actualmente se fuerza que use la última identidad, pero debería ser la identidad seleccionada en el front
        }catch (Exception e){
            throw new HechoMappingException("Se produjo un error al mapear el hecho.");
        }
        try {
            autor.contribuirAlHecho(hecho);
        }catch (Exception e){
            throw new ContribuyenteAssignmentException("No se pudo asignar el contribuyente al hecho");
        }
        try {
            return guardarHecho(hecho);
        }catch (Exception e){
            throw new HechoStorageException("No se pudo guardar el hecho en la base de datos");
        }
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
