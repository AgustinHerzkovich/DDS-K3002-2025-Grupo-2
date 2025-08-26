package domain.services;

import domain.dto.HechoDTO;
import domain.dto.HechoEdicionDTO;
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
        Hecho hecho = new HechoMapper().map(hechoDto, autor.getUltimaIdentidad());
        autor.contribuirAlHecho(hecho);
        return guardarHecho(hecho);
    }

    public Hecho obtenerHecho(String id) {
        return repositorioDeHechos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hecho no encontrado con ID: " + id));
    }

    public Hecho modificarEstadoRevision(String id, EstadoRevision nuevoEstado) {
        Hecho hecho = repositorioDeHechos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hecho no encontrado con ID: " + id));
        hecho.setEstadoRevision(nuevoEstado);
        return repositorioDeHechos.save(hecho);
    }

    public Hecho editarHecho(String id, HechoEdicionDTO hechoEdicionDto) {
        Hecho hecho = repositorioDeHechos.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hecho no encontrado con ID: " + id));
        hecho.editar(hechoEdicionDto.getTitulo(),
                hechoEdicionDto.getDescripcion(),
                hechoEdicionDto.getCategoria(),
                hechoEdicionDto.getUbicacion(),
                hechoEdicionDto.getFechaAcontecimiento(),
                hechoEdicionDto.getContenidoTexto(),
                hechoEdicionDto.getContenidoMultimedia());
        return repositorioDeHechos.save(hecho);
    }
}
