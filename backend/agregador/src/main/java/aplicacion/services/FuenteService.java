package aplicacion.services;

import aplicacion.domain.colecciones.fuentes.*;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.FuenteInputMapper;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.domain.hechos.Hecho;
import aplicacion.repositorios.RepositorioDeFuentes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FuenteService {
    private final RepositorioDeFuentes repositorioDeFuentes;
    private final FuenteInputMapper fuenteInputMapper;
    private final HechoInputMapper hechoInputMapper;
    //@PersistenceContext
    //private EntityManager entityManager;

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes,
                         FuenteInputMapper fuenteInputMapper,
                         HechoInputMapper hechoInputMapper) {
        this.repositorioDeFuentes = repositorioDeFuentes;
        this.fuenteInputMapper = fuenteInputMapper;
        this.hechoInputMapper = hechoInputMapper;
    }

    @Transactional
    public Fuente guardarFuente(Fuente fuente) {
        return repositorioDeFuentes.save(fuente);
    }

    // Busca si una fuente ya existe segun su id. Si no existe la guarda y la devuelve, si ya existe la devuelve.
    @Transactional
    public Fuente guardarFuenteSiNoExiste(Fuente fuente) {
        Optional<Fuente> existente = repositorioDeFuentes.findById(fuente.getId());
        return existente.orElseGet(() -> repositorioDeFuentes.save(fuente));
    }

    @Transactional
    public void guardarFuentes(List<FuenteInputDto> fuentesDto) {
        List<Fuente> fuentes = fuentesDto.stream().map(fuenteInputMapper::map).toList();
        for (Fuente fuente: fuentes) {
            guardarFuente(fuente); // Se guarda las fuentes que no existan en el repositorio, porque podría ocurrir que entre colecciones repitan fuentes
        }
    }

    @Transactional
    public void guardarFuentesSiNoExisten(List<FuenteInputDto> fuentesDto) {
        List<Fuente> fuentes = fuentesDto.stream().map(fuenteInputMapper::map).toList();
        for (Fuente fuente: fuentes) {
            guardarFuenteSiNoExiste(fuente); // Las fuentes que ya existen no se guardan
        }
    }

    @Transactional
    public Map<Fuente, List<Hecho>> hechosUltimaPeticion(List<Fuente> fuentes) { // Retornamos una lista de pares, donde el primer elemento es la lista de hechos y el segundo elemento es la fuente de donde se obtuvieron los hechos
        Map<Fuente, List<Hecho>> hashMap = new HashMap<>();

        for (Fuente fuente : fuentes) {
            //List<Hecho> hechos = new ArrayList<>(); // Lista de hechos que se van a retornar
            List<HechoInputDto> hechosDto = fuente.getHechosUltimaPeticion();

            List<Hecho> hechos = hechosDto.stream().map(hechoInputMapper::map).toList();
            guardarFuente(fuente); // Updateo la fuente
            //entityManager.flush(); // En teoria fuerza la actualizacion
            System.out.println("Fuente " + fuente.getId() + " actualizada con última petición: " + fuente.getUltimaPeticion());
            hashMap.put(fuente, hechos);
        }
        return hashMap;
    }

    public Long obtenerCantidadFuentes() {
        return repositorioDeFuentes.count();
    }


    @Transactional
    public List<Hecho> obtenerHechosPorFuente(String idFuente) {
        return repositorioDeFuentes.findHechosByFuenteId(idFuente);
    }

    public Fuente obtenerFuentePorId(String fuenteId) throws FuenteNoEncontradaException {
        return repositorioDeFuentes.findById(fuenteId).orElseThrow(()->new FuenteNoEncontradaException("No se encontró la fuente con id: " + fuenteId));
    }
}