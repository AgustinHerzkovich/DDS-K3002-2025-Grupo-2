package aplicacion.config;

import aplicacion.exceptions.NoInstanceException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class ConfigService {
    Cache<String, String> cache = Caffeine.newBuilder().maximumSize(10).expireAfterWrite(2, TimeUnit.SECONDS).build();
    private final DiscoveryClient discoveryClient;

    public ConfigService(DiscoveryClient discoveryClient) throws IOException {
        this.discoveryClient = discoveryClient;
    }
    @Value("${agregador.id}")
    private String agregadorID;

    public String getUrlAgregador() {

        return cache.get("agregador", srv -> discoveryClient.getInstances("agregador")
                .stream()
                .filter(instance -> Objects.equals(instance.getMetadata().get("agregadorID"), agregadorID))
                .findFirst()
                .orElseThrow(() -> new NoInstanceException("agregador"))
                .getUri()
                .toString()
                .concat("/" + "agregador")
        );
    }

    public String getUrlEstadisticas() {
        return discoveryClient.getInstances("estadisticas")
                .stream()
                .filter(instance -> instance.getMetadata().get("estadisticasID").equals(agregadorID))
                .findFirst()
                .orElseThrow(() -> new NoInstanceException("estadisticas"))
                .getUri()
                .toString()
                .concat("/estadisticas");
    }

    public String getUrlFuentesDinamicas() {
        return getUrl("fuentesDinamicas");
    }

    private String getUrl(String serviceId) {
        return cache.get(serviceId, srv -> discoveryClient.getInstances(serviceId)
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new NoInstanceException(serviceId))
                                .getUri()
                                .toString()
                                .concat("/" + serviceId)
        );
    }
}
