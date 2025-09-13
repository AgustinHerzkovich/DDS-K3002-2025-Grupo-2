package aplicacion.services;

import aplicacion.domain.colecciones.fuentes.*;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.FuenteInputMapper;
import aplicacion.dto.mappers.FuenteOutputMapper;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.dto.output.FuenteOutputDto;
import aplicacion.excepciones.ColeccionNoEncontradaException;
import aplicacion.repositorios.RepositorioDeFuentesXColeccion;
import aplicacion.repositorios.RepositorioDeHechosXFuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.repositorios.RepositorioDeFuentes;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FuenteService {
    private final RepositorioDeFuentes repositorioDeFuentes;
    private final RepositorioDeHechosXFuente repositorioDeHechosXFuente;
    private final ColeccionService coleccionService;
    private final RepositorioDeFuentesXColeccion repositorioDeFuentesXColeccion;
    private final FuenteInputMapper fuenteInputMapper;
    private final FuenteOutputMapper fuenteOutputMapper;
    private final HechoInputMapper hechoInputMapper;

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes,
                         RepositorioDeHechosXFuente repositorioDeHechosXFuente,
                         ColeccionService coleccionService,
                         RepositorioDeFuentesXColeccion repositorioDeFuentesXColeccion,
                         FuenteInputMapper fuenteInputMapper,
                         FuenteOutputMapper fuenteOutputMapper,
                         HechoInputMapper hechoInputMapper) {
        this.repositorioDeFuentes = repositorioDeFuentes;
        this.coleccionService = coleccionService;
        this.repositorioDeHechosXFuente = repositorioDeHechosXFuente;
        this.repositorioDeFuentesXColeccion = repositorioDeFuentesXColeccion;
        this.fuenteInputMapper = fuenteInputMapper;
        this.fuenteOutputMapper = fuenteOutputMapper;
        this.hechoInputMapper = hechoInputMapper;
    }

    @Transactional
    public void guardarFuente(Fuente fuente) {
        repositorioDeFuentes.save(fuente);
    }

    @Transactional
    public void guardarFuentes(List<FuenteInputDto> fuentesDto) {
        List<Fuente> fuentes = fuentesDto.stream().map(fuenteInputMapper::map).toList();
        for (Fuente fuente: fuentes) {
            guardarFuente(fuente); // Se guarda las fuentes que no existan en el repositorio, porque podría ocurrir que entre colecciones repitan fuentes
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
            hashMap.put(fuente, hechos);
        }
        return hashMap;
    }

    public Long obtenerCantidadFuentes() {
        return repositorioDeFuentes.count();
    }

    private Boolean seCargaronHechosDeEstaFuente(Fuente fuente) {
        return repositorioDeHechosXFuente.existsByFuenteId(fuente.getId());
    }

    @Transactional
    public List<Hecho> obtenerHechosPorFuente(FuenteId fuenteId){
        return repositorioDeHechosXFuente.findHechosByFuenteId(fuenteId);
    }
    public FuenteOutputDto agregarFuenteAColeccion(String coleccionId, FuenteInputDto fuenteInputDTO) throws ColeccionNoEncontradaException {
        Fuente fuente = fuenteInputMapper.map(fuenteInputDTO);
        repositorioDeFuentes.save(fuente);
        coleccionService.agregarFuenteAColeccion(coleccionId, fuente);
        return fuenteOutputMapper.map(fuente);
    }


}