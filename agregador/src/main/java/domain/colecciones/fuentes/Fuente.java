package domain.colecciones.fuentes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import domain.dto.HechoInEstaticaDTO;
import domain.hechos.Hecho;
import domain.mappers.HechoInEstaticaDTOToHecho;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Entity
public class Fuente{
    @EmbeddedId
    @Getter
    private FuenteId id;
    private TipoFuente tipo;
    private LocalDate ultima_peticion;

    public Fuente(String id_externo, TipoFuente tipo) {
        this.id = new FuenteId(UUID.randomUUID().toString(), id_externo);
        this.id.setId_externo(id_externo);
        this.tipo = tipo;
        this.ultima_peticion = null;
    }

    public Fuente() {

    }

    public String getUrl() {
        String url = "http://localhost:";
        switch (tipo) {
            case ESTATICA:
                url += "8080/fuentesEstaticas/";
                break;
            case DINAMICA:
                url += "8081/fuentesDinamicas/";
                break;
            case PROXY:
                url += "8082/fuentesProxy/";
                break;
        }
        url += id.getId_externo();
        return url;
    }

    // TODO: Ver si esto realmente debe estar en la clase Fuente o si es mejor moverlo a un servicio
    public List<Hecho> hechos() {
        List<Hecho> todosLosHechos = new ArrayList<>();
        String url = getUrl() + "/hechos";
        ObjectMapper mapper = new ObjectMapper(); // Creo un object mapper para mappear el resultado del json a un objeto Hecho
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RestTemplate restTemplate = new RestTemplate();
        HechoInEstaticaDTOToHecho mapper_dto = new HechoInEstaticaDTOToHecho(); // Mapper para mapear de HechoInEstaticaDTO a Hecho
        List<Hecho> hechos;
        if (ultima_peticion != null) { // Si no es la primera vez que se hace una peticion, trae los hechos desde la ultima peticion
            url += "?fechaMayorA=" + ultima_peticion; // TODO: Agregar esto de fechaMayorA a la API de las fuentes
        }
        ultima_peticion = LocalDate.now();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String json = response.getBody();

            if (Objects.requireNonNull(tipo) == TipoFuente.ESTATICA) { // Si la fuente es estatica, mapeo a HechoInEstaticaDTO
                List<HechoInEstaticaDTO> hechos_dto = mapper.readValue(json, new TypeReference<>() {
                });
                hechos = hechos_dto.stream().map(mapper_dto::map).toList();
                todosLosHechos.addAll(hechos);
            } else { // Si la fuente es dinamica o proxy, mapeo a Hecho
                hechos = mapper.readValue(json, new TypeReference<>() {
                });
                todosLosHechos.addAll(hechos);
            }
        } catch (Exception e) {
            System.err.println("Error al consumir la API en " + id.getId_externo() + ": " + e.getMessage());
        }

        return todosLosHechos; // Retorna una lista de hechos obtenidos de la fuente
    }
}
