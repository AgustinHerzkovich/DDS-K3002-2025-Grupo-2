package aplicacion.domain.fuentesProxy;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

// FUENTE PROXY
@Getter
@Entity
@NoArgsConstructor
public abstract class FuenteProxy{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /*public abstract void pedirHechos();
    public abstract List<Hecho> importarHechos(DiscoveryClient discoveryClient);*/
}