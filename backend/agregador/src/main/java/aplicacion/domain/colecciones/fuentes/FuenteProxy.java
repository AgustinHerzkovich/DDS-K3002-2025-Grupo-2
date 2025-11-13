package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.HechoInputDto;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.util.List;

@Entity
@NoArgsConstructor
public class FuenteProxy extends Fuente {

    public FuenteProxy(String id) {
        super(id,"FUENTEPROXY");
    }
    public FuenteProxy(String id, String nombreServicio) {
        super(id, nombreServicio);
    }

    @Override
    public String pathIntermedio() {
        return "fuentesProxy/" + this.getId();
    }



    @Override
    protected String obtenerURL(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient, ServiceInstance instance) {
        return instance.getUri().toString() + hechosPathParam();
    }

    @Override
    protected String hechosPathParam() {
        return "/fuentesProxy/" + this.getId() + "/hechos";
    }


}
