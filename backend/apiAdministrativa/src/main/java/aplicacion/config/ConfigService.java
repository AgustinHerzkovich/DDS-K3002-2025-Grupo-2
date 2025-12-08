package aplicacion.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import org.springframework.stereotype.Service;


@Service
@Getter
@Setter
public class ConfigService {

    private DiscoveryClient discoveryClient;

    public ConfigService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public String getUrlAgregador() {
        ServiceInstance instance = discoveryClient.getInstances("agregador").getFirst();
        return instance.getUri() + "/agregador";
    }

    public String getUrlFuentesEstaticas() {
        String placeholder = "fuentesEstaticas";
        return discoveryClient.getInstances(placeholder)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay instancias de " + placeholder + " registradas"))
                .getUri()
                .toString()
                .concat("/" + placeholder);
    }


    public String getUrlFuentesProxy() {
        String placeholder = "fuentesProxy";
        return discoveryClient.getInstances(placeholder)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay instancias de " + placeholder + " registradas"))
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
}
