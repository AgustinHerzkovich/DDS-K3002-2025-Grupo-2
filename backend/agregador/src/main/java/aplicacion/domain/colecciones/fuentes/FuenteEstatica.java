package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.conexiones.Conexion;
import aplicacion.dto.input.HechoInputDto;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class FuenteEstatica extends Fuente {
    private Boolean fueConsultada = false;

    public FuenteEstatica(String id) {
        super(id,"FUENTEESTATICA");
        this.fueConsultada = false;
    }

    public FuenteEstatica(String fuenteId, String serviceId) {
        super(fuenteId, serviceId);
        this.fueConsultada = false;
    }

    @Override
    public List<HechoInputDto> getHechosUltimaPeticion(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient, ServiceInstance instance) {
        if (this.fueConsultada) {
            return new ArrayList<>();
        }
        this.fueConsultada = true;
        return super.getHechosUltimaPeticion(discoveryClient, loadBalancerClient, instance);
    }

    @Override
    public String pathIntermedio() {
        return "fuentesEstaticas/" + this.getId();
    }

    @Override
    protected String hechosPathParam() {
        return "/fuentesEstaticas/" + this.getId() + "/hechos";
    }
}
