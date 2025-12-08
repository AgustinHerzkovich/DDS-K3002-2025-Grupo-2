package aplicacion.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public String getUrlAgregador() {
        ServiceInstance instance = discoveryClient.getInstances("agregador").getFirst();

        return instance.getUri() + "/agregador";
    }

    public String getUrlEstadisticas() {
        String placeholder = "estadisticas";
        return discoveryClient.getInstances(placeholder)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay instancias de " + placeholder +  " registradas"))
                .getUri()
                .toString()
                .concat("/" + placeholder);
    }

    public String getUrlFuentesDinamicas() {
        String placeholder = "fuentesDinamicas";
        return discoveryClient.getInstances(placeholder)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay instancias de " + placeholder +  " registradas"))
                .getUri()
                .toString()
                .concat("/" + placeholder);
    }
}
