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
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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



    public Fuente(String id, String nombreServicio) {
        this.id = id;
        this.ultimaPeticion = null;
        this.alias = "Fuente sin título";
        this.nombreServicio = nombreServicio;
    }

    public Fuente(String id) {
        this.id = id;
        this.ultimaPeticion = null;
        this.alias = "Fuente sin título";
    }

    public List<HechoInputDto> getHechosUltimaPeticion(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient, ServiceInstance instance) {

        System.out.print("hola en getHechosUltimaPeticion");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        LocalDateTime fechaAnterior = this.getUltimaPeticion();
        List<HechoInputDto> hechos = new ArrayList<>();

        String url = obtenerURL(discoveryClient, loadBalancerClient, instance);


        if (fechaAnterior != null) {
            url += "?fechaMayorA=" + fechaAnterior;
        }

        this.setUltimaPeticion(LocalDateTime.now());

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response;
            String json;
            System.out.println(url);
            response = restTemplate.getForEntity(url, String.class);
            json = response.getBody();
            hechos = mapper.readValue(json, new TypeReference<>() {});
            System.out.println("Se recibieron correctamente los hechos de la fuente " + this.getId() + " " + this.getAlias());
        } catch (Exception e) {
            this.setUltimaPeticion(fechaAnterior); // rollback si falla
            System.err.println("⚠️ Error al consumir la API en fuente " + this.getId() + ": " + e.getMessage());
        }

        return hechos;
    }

    public abstract String pathIntermedio();

    public void agregarHechos(List<Hecho> hechosAAgregar) {
        this.hechos.addAll(hechosAAgregar);
    }

    public void eliminarTodosLosHechos() {
        this.hechos.clear();
    }

    protected String obtenerURL(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient, ServiceInstance _instance) {
        if (_instance != null) {
            return _instance.getUri().toString() + hechosPathParam();
        }
        return loadBalancerClient.choose(nombreServicio).getUri().toString() + hechosPathParam();

    }
    protected abstract String hechosPathParam();
}
