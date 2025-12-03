package aplicacion.services;

import aplicacion.domain.colecciones.fuentes.*;
import aplicacion.dto.input.FuenteAliasDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.mappers.FuenteInputMapper;
import aplicacion.dto.mappers.HechoInputMapper;
import aplicacion.excepciones.FuenteNoEncontradaException;
import aplicacion.domain.hechos.Hecho;
import aplicacion.excepciones.TipoDeFuenteErroneoException;
import aplicacion.repositories.FuenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FuenteService {
    private final FuenteRepository fuenteRepository;
    private final FuenteInputMapper fuenteInputMapper;
    private final HechoInputMapper hechoInputMapper;
    //@PersistenceContext
    //private EntityManager entityManager;

    public FuenteService(FuenteRepository fuenteRepository,
                         FuenteInputMapper fuenteInputMapper,
                         HechoInputMapper hechoInputMapper) {
        this.fuenteRepository = fuenteRepository;
        this.fuenteInputMapper = fuenteInputMapper;
        this.hechoInputMapper = hechoInputMapper;
    }

    public Page<Fuente> findByTipo(Integer page, Integer limit, String tipo){
        if(tipo == null)
            return fuenteRepository.findAll(PageRequest.of(page, limit));
        Class<? extends  Fuente> tipoClass = switch (tipo.toLowerCase()) {
            case "estatica", "estática", "e" -> FuenteEstatica.class;
            case "dinamica", "dinámica", "d" -> FuenteDinamica.class;
            case "proxy", "p" -> FuenteProxy.class;
            default -> throw new TipoDeFuenteErroneoException("Tipo de fuente no reconocido: '"+tipo+"'.Se acepta estatica, dinamica y proxy");
        };

        return fuenteRepository.findByTipo(tipoClass, PageRequest.of(page, limit));
    }

    @Transactional
    public Fuente guardarFuente(Fuente fuente) {
        return fuenteRepository.save(fuente);
    }
    /**
     * Recibe una Fuente
     * Busca en la BD si la fuente existe por su ID
     * Si existe, modifica su campo conexion por la conexion de la fuente recibida
     * Si no existe, la crea
     * */

    @Transactional
    public Fuente guardarOActualizarConexion(Fuente fuente) {
        Optional<Fuente> optFuente = fuenteRepository.findById(fuente.getId());
        if(optFuente.isEmpty())
            return fuenteRepository.save(fuente);
        optFuente.get().setConexion(fuente.getConexion());
        return fuenteRepository.save(optFuente.get());

    }

    // Busca si una fuente ya existe segun su id. Si no existe la guarda y la devuelve, si ya existe la devuelve.
    @Transactional
    public Fuente guardarFuenteSiNoExiste(Fuente fuente) {
        Optional<Fuente> existente = fuenteRepository.findById(fuente.getId());
        return existente.orElseGet(() -> fuenteRepository.save(fuente));
    }

    @Transactional
    public void guardarFuentes(List<FuenteInputDto> fuentesDto) {
        List<Fuente> fuentes = fuentesDto.stream().map(fuenteInputMapper::map).toList();
        for (Fuente fuente: fuentes) {
            guardarFuente(fuente); // Se guarda las fuentes que no existan en el repositorio, porque podría ocurrir que entre colecciones repitan fuentes
        }
    }

    @Transactional
    public void guardarFuentesSiNoExisten(List< ? extends FuenteInputDto> fuentesDto) {
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

    public Long obtenerCantidadFuentesConHechos() {
        return fuenteRepository.countWithHechos();
    }


    @Transactional
    public List<Hecho> obtenerHechosPorFuente(String idFuente) {
        return fuenteRepository.findHechosByFuenteId(idFuente);
    }

    public Fuente obtenerFuentePorId(String fuenteId) throws FuenteNoEncontradaException {
        return fuenteRepository.findById(fuenteId).orElseThrow(()->new FuenteNoEncontradaException("No se encontró la fuente con id: " + fuenteId));
    }

    public List<Fuente> obtenerTodasLasFuentes(){
        return fuenteRepository.findAll();
    }
    @Transactional
    public Fuente cambiarAlias(String id, FuenteAliasDto fuenteAliasDto) {
        Fuente fuente = obtenerFuentePorId(id);
        fuente.setAlias(fuenteAliasDto.getAlias());
        fuenteRepository.save(fuente);
        return fuente;
    }
}