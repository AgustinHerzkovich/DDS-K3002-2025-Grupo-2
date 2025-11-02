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
    private String alias;
    private LocalDateTime ultimaPeticion;
    @ManyToOne // Una conexion puede dar lugar a multiples fuentes
    private Conexion conexion;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "hecho_fuente", joinColumns = @JoinColumn(name = "fuente_id"), inverseJoinColumns = @JoinColumn(name = "hecho_id"))
    private List<Hecho> hechos;

    public Fuente(String id, Conexion conexion) {
        this.id = id;
        this.ultimaPeticion = null; // Arranca en null para que si es la primera petición, traer todos los hechos
        this.conexion = conexion;
        this.hechos = new ArrayList<>();
        this.alias = "Fuente sin titulo";
    }
    public Fuente(String id){
        this.id = id;
        this.ultimaPeticion = null;
        this.hechos = new ArrayList<>();
        this.alias = "Fuente sin titulo";
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
        return conexion.construirURI() + "/" + this.pathIntermedio() + "/hechos";
    }

    public abstract String pathIntermedio();

    public void agregarHechos(List<Hecho> hechosAAgregar){
        hechos.addAll(hechosAAgregar);
    }
    public void eliminarTodosLosHechos(){
        hechos.clear();
    }
}