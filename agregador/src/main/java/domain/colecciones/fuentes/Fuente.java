package domain.colecciones.fuentes;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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