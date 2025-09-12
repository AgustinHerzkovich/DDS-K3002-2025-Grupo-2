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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import aplicacion.config.ConfiguracionRed;
import aplicacion.domain.hechos.Hecho;
import aplicacion.repositorios.RepositorioDeFuentes;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FuenteService {
    private final RepositorioDeFuentes repositorioDeFuentes;
    private final ConfiguracionRed config;
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
        this.config = cargarConfiguracion();
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
    public void guardarFuentes(List<Fuente> fuentes) {
        for (Fuente fuente: fuentes) {
            guardarFuente(fuente); // Se guarda las fuentes que no existan en el repositorio, porque podría ocurrir que entre colecciones repitan fuentes
        }
    }

    private ConfiguracionRed cargarConfiguracion() { // Metodo para cargar la configuración de red desde un archivo JSON
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.json");
            if (input == null) {
                throw new RuntimeException("No se encontró config.json en resources");
            }
            return mapper.readValue(input, ConfiguracionRed.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar configuración", e);
        }
    }

    @Transactional
    public Map<Fuente, List<Hecho>> hechosUltimaPeticion(List<Fuente> fuentes) { // Retornamos una lista de pares, donde el primer elemento es la lista de hechos y el segundo elemento es la fuente de donde se obtuvieron los hechos
        Map<Fuente, List<Hecho>> hashMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(); // Creo un object mapper para mappear el resultado del json a un objeto Hecho
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RestTemplate restTemplate = new RestTemplate();

        for (Fuente fuente : fuentes) {
            List<Hecho> hechos = new ArrayList<>(); // Lista de hechos que se van a retornar
            // Armo la url a la cual consultar según la fuente
            String ip = "";
            Integer puerto = 0;
            String tipo = "";
            String url;
            TipoFuente tipoFuente = fuente.getId().getTipo();
            switch (tipoFuente) {
                case ESTATICA:
                    ip = config.ip_estatica;
                    puerto = config.puerto_estatica;
                    tipo = "fuentesEstaticas";
                    break;
                case DINAMICA:
                    ip = config.ip_dinamica;
                    puerto = config.puerto_dinamica;
                    tipo = "fuentesDinamicas";
                    break;
                case PROXY:
                    ip = config.ip_proxy;
                    puerto = config.puerto_proxy;
                    tipo = "fuentesProxy";
                    break;
            }
            if (tipoFuente == TipoFuente.DINAMICA) { // TODO: Cambiar
                 url = "http://" + ip + ":" + puerto + "/" + tipo + "/hechos";
            } else {
                url = "http://" + ip + ":" + puerto + "/" + tipo + "/" + fuente.getId().getIdExterno() + "/hechos";
            }

            LocalDateTime fecha = fuente.getUltimaPeticion();
            if (fecha != null) {
                url += "?fechaMayorA=" + fecha;
            }

            fuente.setUltimaPeticion(LocalDateTime.now()); // actualizar fuente con la fecha de la ultima peticion
            try {
                ResponseEntity<String> response;
                String json;

                if ((Objects.requireNonNull(fuente.getId().getTipo()) == TipoFuente.ESTATICA && !this.seCargaronHechosDeEstaFuente(fuente)) || Objects.requireNonNull(fuente.getId().getTipo()) != TipoFuente.ESTATICA) { // // Esta es la validación que evita reprocesar hechos de fuentes estáticas
                    response = restTemplate.getForEntity(url, String.class);
                    json = response.getBody();
                    List<HechoInputDto> hechosDto = mapper.readValue(json, new TypeReference<>() {
                    });
                    hechos = hechosDto.stream().map(hechoInputMapper::map).toList();
                }
                hashMap.put(fuente, hechos); // Agrego la lista de hechos y la fuente a la lista de pares

            } catch (Exception e) {
                fuente.setUltimaPeticion(fecha); // Si hubo un error, no actualizo la fecha de la ultima peticion
                System.err.println("Error al consumir la API en " + fuente.getId().getIdExterno() + ": " + e.getMessage());
            }
            guardarFuente(fuente); // Updateo la fuente
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