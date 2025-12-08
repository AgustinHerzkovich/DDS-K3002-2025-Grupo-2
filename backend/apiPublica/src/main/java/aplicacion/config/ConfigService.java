package aplicacion.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConfigService {

    private final DiscoveryClient discoveryClient;

    public ConfigService(DiscoveryClient discoveryClient) throws IOException {
        this.discoveryClient = discoveryClient;
    }
    @Value("${agregador.id}")
    private String agregadorID;

    public String getUrlAgregador() {
        return discoveryClient.getInstances("agregador")
                .stream()
                .filter(instance -> instance.getMetadata().get("agregadorID") != agregadorID)
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
