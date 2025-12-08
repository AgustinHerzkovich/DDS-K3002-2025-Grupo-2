package aplicacion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class ConfigService {

    private final DiscoveryClient discoveryClient;

    public ConfigService(DiscoveryClient discoveryClient) throws IOException {
        this.discoveryClient = discoveryClient;
    }
    @Value("${agregador.id}")
    private String agregadorID;

    public String getUrlAgregador() {
        System.out.println("AGREGADOR ID EN CONFIG SERVICE: " + discoveryClient.getInstances("agregador")
                .stream()
                .map(instance -> instance.getMetadata().get("agregadorID"))
                .toList()
        );

        return discoveryClient.getInstances("agregador")
                .stream()
                .filter(instance -> Objects.equals(instance.getMetadata().get("agregadorID"), agregadorID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay instancias de " + "agregador" +  " registradas"))
                .getUri()
                .toString()
                .concat("/" + "agregador");
    }

    public String getUrlEstadisticas() {
        return getUrl("estadisticas");
    }

    public String getUrlFuentesDinamicas() {
        return getUrl("fuentesDinamicas");
    }

    private String getUrl(String serviceId) {
        return discoveryClient.getInstances(serviceId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay instancias de " + serviceId +  " registradas"))
                .getUri()
                .toString()
                .concat("/" + serviceId);
    }

}
