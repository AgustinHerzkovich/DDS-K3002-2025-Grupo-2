package aplicacion.config.metadataConfig;

import com.netflix.appinfo.ApplicationInfoManager;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableScheduling
public class EurekaMetadataConfig {

    private final ApplicationInfoManager appInfoManager;
    @Value("${fuente.id}")
    private String fuenteId;

    public EurekaMetadataConfig(ApplicationInfoManager appInfoManager) {
        this.appInfoManager = appInfoManager;

    }

    @PostConstruct
    public void inicializarMetadata() {
        appInfoManager.getInfo().getMetadata()
                .put("fuentes", fuenteId);

        // Fuerza que Eureka reciba la actualización
        appInfoManager.refreshDataCenterInfoIfRequired();
        appInfoManager.registerAppMetadata(appInfoManager.getInfo().getMetadata());
    }
}
