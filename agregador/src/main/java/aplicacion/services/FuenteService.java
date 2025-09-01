package aplicacion.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import aplicacion.domain.colecciones.fuentes.TipoFuente;
import aplicacion.config.ConfiguracionRed;
import aplicacion.dto.HechoInEstaticaDTO;
import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.mappers.HechoInEstaticaDTOToHecho;
import aplicacion.repositorios.RepositorioDeFuentes;
import aplicacion.domain.colecciones.fuentes.Fuente;
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

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes) {
        this.repositorioDeFuentes = repositorioDeFuentes;
        this.config = cargarConfiguracion();
    }

    @Transactional
    public void guardarFuente(Fuente fuente) {
        if (!repositorioDeFuentes.existsById(fuente.getId())) {
            repositorioDeFuentes.save(fuente); // Se guarda la fuente en el repositorio si es que no existía ya
        }
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

    public Map<Fuente, List<Hecho>> hechosUltimaPeticion(List<Fuente> fuentes) { // Retornamos una lista de pares, donde el primer elemento es la lista de hechos y el segundo elemento es la fuente de donde se obtuvieron los hechos
        Map<Fuente, List<Hecho>> hashMap = new HashMap<>();
        List<Hecho> hechos; // Lista de hechos que se van a retornar
        ObjectMapper mapper = new ObjectMapper(); // Creo un object mapper para mappear el resultado del json a un objeto Hecho
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RestTemplate restTemplate = new RestTemplate();
        HechoInEstaticaDTOToHecho mapperDto = new HechoInEstaticaDTOToHecho(); // Mapper para mapear de HechoInEstaticaDTO a Hecho

        for (Fuente fuente : fuentes) {
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
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                String json = response.getBody();

                if (Objects.requireNonNull(fuente.getId().getTipo()) == TipoFuente.ESTATICA) { // Si la fuente es estatica, mapeo a HechoInEstaticaDTO
                    List<HechoInEstaticaDTO> hechosDto = mapper.readValue(json, new TypeReference<>() {
                    });
                    hechos = hechosDto.stream().map(mapperDto::map).toList();
                } else { // Si la fuente es dinamica o proxy, mapeo a Hecho
                    hechos = mapper.readValue(json, new TypeReference<>() {
                    });
                }
                hashMap.put(fuente, hechos); // Agrego la lista de hechos y la fuente a la lista de pares

            } catch (Exception e) {
                fuente.setUltimaPeticion(fecha); // Si hubo un error, no actualizo la fecha de la ultima peticion
                System.err.println("Error al consumir la API en " + fuente.getId().getIdExterno() + ": " + e.getMessage());
            }
        }
        return hashMap;
    }

    public Integer obtenerCantidadFuentes() {
        return Math.toIntExact(repositorioDeFuentes.count());
    }
}