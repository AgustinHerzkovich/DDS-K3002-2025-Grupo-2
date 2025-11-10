package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.domain.conexiones.Conexion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
public abstract class Fuente {

    @Id
    private String id;
    private String alias;
    private LocalDateTime ultimaPeticion;

    private String nombreServicio;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "hecho_fuente",
            joinColumns = @JoinColumn(name = "fuente_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id"))
    private List<Hecho> hechos = new ArrayList<>();

    @Autowired
    @Qualifier("eurekaRestTemplate")
    private RestTemplate eurekaRestTemplate;

    @Autowired
    @Qualifier("plainRestTemplate")
    private RestTemplate plainRestTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public Fuente(String id, String nombreServicio) {
        this.id = id;
        this.ultimaPeticion = null;
        this.nombreServicio = nombreServicio;
        this.alias = "Fuente sin título";
    }

    public Fuente(String id) {
        this.id = id;
        this.ultimaPeticion = null;
        this.alias = "Fuente sin título";
    }
    public Fuente(String id, String nombreServicio, String instanceID){
        this.id = id;
        this.ultimaPeticion = null;
        this.nombreServicio = nombreServicio;
        this.alias = "Fuente sin titulo";
    }

    public List<HechoInputDto> getHechosUltimaPeticion() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        LocalDateTime fechaAnterior = this.getUltimaPeticion();
        List<HechoInputDto> hechos = new ArrayList<>();
        String url = this.getUrl();

        if (fechaAnterior != null) {
            url += "?fechaMayorA=" + fechaAnterior;
        }

        this.setUltimaPeticion(LocalDateTime.now());

        try {
            ResponseEntity<String> response;
            String json;

            if (this instanceof FuenteProxy proxy) {
                // 🔸 FuenteProxy → instancia específica vía DiscoveryClient
                String resolvedUrl = proxy.resolverUrlProxy(discoveryClient);
                response = plainRestTemplate.getForEntity(resolvedUrl, String.class);
            } else {
                // 🔸 FuenteEstatica o FuenteDinamica → Eureka (service discovery)
                response = eurekaRestTemplate.getForEntity(url, String.class);
            }

            json = response.getBody();
            hechos = mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            this.setUltimaPeticion(fechaAnterior); // rollback si falla
            System.err.println("⚠️ Error al consumir la API en fuente " + this.getId() + ": " + e.getMessage());
        }

        return hechos;
    }

    private String getUrl() {
        return "http://" + nombreServicio + "/" + this.pathIntermedio() + "/hechos";
    }

    public abstract String pathIntermedio();

    public void agregarHechos(List<Hecho> hechosAAgregar) {
        this.hechos.addAll(hechosAAgregar);
    }

    public void eliminarTodosLosHechos() {
        this.hechos.clear();
    }
}
