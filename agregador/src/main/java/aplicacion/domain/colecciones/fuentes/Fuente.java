package aplicacion.domain.colecciones.fuentes;

import aplicacion.dto.input.HechoInputDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public abstract class Fuente{
    @Id
    private String id;
    private LocalDateTime ultimaPeticion;
    @Column(length = 25)
    private String ip;
    private Integer puerto;

    public Fuente(String id, String ip, Integer puerto) {
        this.id = id;
        this.ultimaPeticion = null; // Arranca en null para que si es la primera petición, traer todos los hechos
        this.ip = ip;
        this.puerto = puerto;
    }

    public List<HechoInputDto> getHechosUltimaPeticion() {
        ObjectMapper mapper = new ObjectMapper(); // Creo un object mapper para mappear el resultado del json a un objeto Hecho
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RestTemplate restTemplate = new RestTemplate();

        LocalDateTime fecha = this.getUltimaPeticion();
        List<HechoInputDto> hechos = new ArrayList<>();

        String url = this.getUrl();
        if (this.getUltimaPeticion() != null) {
            url += "?fechaMayorA=" + this.getUltimaPeticion();
        }


        this.setUltimaPeticion(LocalDateTime.now());

        try {
            ResponseEntity<String> response;
            String json;

            response = restTemplate.getForEntity(url, String.class);
            json = response.getBody();
            hechos = mapper.readValue(json, new TypeReference<>() {
            });

        } catch (Exception e) {
            this.setUltimaPeticion(fecha); // Si hubo un error, no actualizo la fecha de la ultima peticion
            System.err.println("Error al consumir la API en " + this.getId() + ": " + e.getMessage());
        }

        return hechos;
    }

    private String getUrl() {
        return "http://" + this.getIp() + ":" + this.getPuerto() + "/" + this.pathIntermedio() + "/hechos";
    }

    public abstract String pathIntermedio();
}