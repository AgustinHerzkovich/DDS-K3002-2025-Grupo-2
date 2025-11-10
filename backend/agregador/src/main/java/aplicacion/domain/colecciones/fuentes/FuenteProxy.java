package aplicacion.domain.colecciones.fuentes;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

@Entity
@NoArgsConstructor
public class FuenteProxy extends Fuente {
    private String instanceID;

    public FuenteProxy(String id) {
        super(id);
    }
    public FuenteProxy(String id, String nombreServicio, String instanceID) {
        super(id, nombreServicio);
        this.instanceID = instanceID;
    }

    @Override
    public String pathIntermedio() {
        return "fuentesProxy/" + this.getId();
    }



    @Override
    protected String obtenerURL(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient) {
        return discoveryClient.getInstances(this.getNombreServicio()).stream()
                .filter(instance -> instance.getInstanceId().equals(instanceID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró la instancia " + instanceID + " para el servicio " + this.getNombreServicio()
                ))
                .getUri()
                .toString();
    }

    @Override
    protected String hechosPathParam() {
        return "fuentesProxy/" + this.getId() + "/hechos";
    }

}
