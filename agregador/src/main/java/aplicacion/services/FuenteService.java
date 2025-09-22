package aplicacion.services;

import aplicacion.domain.colecciones.fuentes.*;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.FuenteInputMapper;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.excepciones.FuenteNoEncontradaException;
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
    private final FuenteInputMapper fuenteInputMapper;
    private final HechoInputMapper hechoInputMapper;

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes,
                         RepositorioDeHechosXFuente repositorioDeHechosXFuente,
                         FuenteInputMapper fuenteInputMapper,
                         HechoInputMapper hechoInputMapper) {
        this.repositorioDeFuentes = repositorioDeFuentes;
        this.repositorioDeHechosXFuente = repositorioDeHechosXFuente;
        this.fuenteInputMapper = fuenteInputMapper;
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


    @Transactional
    public List<Hecho> obtenerHechosPorFuente(String fuenteId){
        return repositorioDeHechosXFuente.findHechosByFuenteId(fuenteId);
    }

    public Fuente obtenerFuentePorId(String fuenteId) throws FuenteNoEncontradaException {
        return repositorioDeFuentes.findById(fuenteId).orElseThrow(()->new FuenteNoEncontradaException("No se encontró la fuente con id: " + fuenteId));
    }
}