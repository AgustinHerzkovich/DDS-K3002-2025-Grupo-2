package domain.services;

import domain.dto.HechoDTO;
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
    public List<Hecho> obtenerHechosConFechaMayorA(LocalDateTime fechaMayorA) {
        return repositorioDeHechos.findAll().stream().filter(hecho -> hecho.getFechaUltimaModificacion().isAfter(fechaMayorA)).toList();
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
}
