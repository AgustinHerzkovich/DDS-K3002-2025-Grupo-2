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
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Entity
public class Fuente{
    @EmbeddedId
    @Getter
    private FuenteId id;
    @Getter
    private TipoFuente tipo;
    @Getter
    @Setter
    private LocalDateTime ultimaPeticion;

    public Fuente(String idExterno, TipoFuente tipo) {
        this.id = new FuenteId(UUID.randomUUID().toString(), idExterno);
        this.id.setIdExterno(idExterno);
        this.tipo = tipo;
        this.ultimaPeticion = null; // Arranca en null para que si es la primera petición, traer todos los hechos
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
            case PROXY_METAMAPA:
                url += "8082/fuentesMetamapa/";
                break;
            case PROXY_DEMO:
                url += "8082/fuentesDemo/";
                break;
        }
        url += id.getIdExterno();
        return url;
    }
}