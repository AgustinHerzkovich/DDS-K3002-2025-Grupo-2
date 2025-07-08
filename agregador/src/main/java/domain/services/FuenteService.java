package domain.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.colecciones.fuentes.TipoFuente;
import domain.dto.HechoInEstaticaDTO;
import domain.hechos.Hecho;
import domain.mappers.HechoInEstaticaDTOToHecho;
import domain.repositorios.RepositorioDeFuentes;
import domain.colecciones.fuentes.Fuente;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FuenteService {
    private final RepositorioDeFuentes repositorioDeFuentes;

    public FuenteService(RepositorioDeFuentes repositorioDeFuentes) {
        this.repositorioDeFuentes = repositorioDeFuentes;
    }

    public void guardarFuentes(List<Fuente> fuentes) {
        repositorioDeFuentes.saveAllIfNotExists(fuentes); // Se guarda las fuentes que no existan en el repositorio, porque podría ocurrir que entre colecciones repitan fuentes
    }

    public Map<Fuente, List<Hecho>> hechosUltimaPeticion() { // Retornamos una lista de pares, donde el primer elemento es la lista de hechos y el segundo elemento es la fuente de donde se obtuvieron los hechos
        List<Fuente> fuentes = repositorioDeFuentes.findAll();
        Map<Fuente, List<Hecho>> hashMap = new HashMap<>();
        List<Hecho> hechos; // Lista de hechos que se van a retornar
        ObjectMapper mapper = new ObjectMapper(); // Creo un object mapper para mappear el resultado del json a un objeto Hecho
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RestTemplate restTemplate = new RestTemplate();
        HechoInEstaticaDTOToHecho mapperDto = new HechoInEstaticaDTOToHecho(); // Mapper para mapear de HechoInEstaticaDTO a Hecho

        for (Fuente fuente : fuentes) {
            String url = fuente.getUrl() + "/hechos";
            LocalDateTime fecha = fuente.getUltimaPeticion();
            if (fecha != null) {
                url += "?fechaMayorA=" + fecha;
            }
            fuente.setUltimaPeticion(LocalDateTime.now()); // actualizar fuente con la fecha de la ultima peticion

            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                String json = response.getBody();

                if (Objects.requireNonNull(fuente.getTipo()) == TipoFuente.ESTATICA) { // Si la fuente es estatica, mapeo a HechoInEstaticaDTO
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
}



