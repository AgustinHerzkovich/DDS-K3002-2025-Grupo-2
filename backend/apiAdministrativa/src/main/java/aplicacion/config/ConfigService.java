package aplicacion.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@Getter
@Setter
public class ConfigService {

    private DiscoveryClient discoveryClient;

    public ConfigService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Value("${agregador.id}")
    private String agregadorID;

    public String getUrlAgregador() {
        return discoveryClient.getInstances("agregador")
                .stream()
                .filter(instance -> Objects.equals(instance.getMetadata().get("agregadorID"), agregadorID))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay instancias de " + "agregador" +  " registradas"))
                .getUri()
                .toString()
                .concat("/" + "agregador");
    }

    public String getUrlFuentesEstaticas() {
        return getUrl("fuentesEstaticas");
    }


    public String getUrlFuentesProxy() {
        return getUrl("fuentesProxy");
    }

    public String getUrlFuentesDinamicas() {
        return getUrl("fuentesDinamicas");
    }

    public String getUrlEstadisticas() {
        return getUrl("estadisticas");
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
