package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.conexiones.Conexion;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@Entity
@NoArgsConstructor
public class FuenteProxy extends Fuente {
    private String instanceID;

    public FuenteProxy(String id, String nombreServicio) {
        super(id, nombreServicio);
    }

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

    public String resolverUrlProxy(DiscoveryClient discoveryClient) {
        String targetService = "cargador-fuente-proxy";

        ServiceInstance instance = discoveryClient.getInstances(targetService).stream()
                .filter(i -> i.getInstanceId().equals(instanceID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró la instancia del proxy con id " + instanceID));

        return instance.getUri() + "/" + this.pathIntermedio() + "/hechos";
    }
}
