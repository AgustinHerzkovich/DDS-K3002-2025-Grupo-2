package aplicacion.config;


import aplicacion.exceptions.NoInstanceException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
@Getter
@Setter
public class ConfigService {
    Cache<String, String> cache = Caffeine.newBuilder().maximumSize(10).expireAfterWrite(30, TimeUnit.SECONDS).build();

    private DiscoveryClient discoveryClient;

    public ConfigService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Value("${agregador.id}")
    private String agregadorID;

    public String getUrlAgregador() {
        return cache.get( "agregador", srv ->
        discoveryClient.getInstances("agregador")
                .stream()
                .filter(instance -> Objects.equals(instance.getMetadata().get("agregadorID"), agregadorID))
                .findFirst()
                .orElseThrow(() -> new NoInstanceException("agregador"))
                .getUri()
                .toString()
                .concat("/" + "agregador"));
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
        return cache.get(serviceId, srv -> discoveryClient.getInstances(serviceId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoInstanceException(serviceId))
                .getUri()
                .toString()
                .concat("/" + serviceId));
    }
}
