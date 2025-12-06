package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.HechoInputDto;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Entity
@NoArgsConstructor
public class FuenteProxy extends Fuente {

    public FuenteProxy(String id) {
        super(id);
    }

    @Override
    public String pathIntermedio() {
        return "fuentesProxy/" + this.getId();
    }



    @Override
    protected String obtenerURL(DiscoveryClient discoveryClient, LoadBalancerClient loadBalancerClient) {
        return discoveryClient.getInstances(getNombreServicio()).stream()
                .filter(i -> {
                    String fuentes = i.getMetadata().get("fuentesDisponibles");
                    return fuentes != null && Arrays.asList(fuentes.split(",")).contains(getId());
                })
                .findFirst()
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() -> new IllegalStateException(
                        "No se encontró ninguna instancia del servicio '"
                                + getNombreServicio() + "' que contenga la fuenteID '" + getId() + "'"
                ));
    }

    @Override
    protected String hechosPathParam() {
        return "/fuentesProxy/" + this.getId() + "/hechos";
    }


}
