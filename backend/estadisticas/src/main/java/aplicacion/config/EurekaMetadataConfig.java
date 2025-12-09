package aplicacion.config;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.UUID;

@Configuration
public class EurekaMetadataConfig {

    @Autowired
    private ApplicationInfoManager appInfoManager;

    private String agregadorID;

    @Value("${estadisticas.id}")
    private String estadisticasID;

    public EurekaMetadataConfig(ApplicationInfoManager appInfoManager) {
        this.appInfoManager = appInfoManager;
    }
    @PostConstruct
    public void inicializarMetadata() {


        appInfoManager.getInfo().getMetadata().put("estadisticasID", estadisticasID);

        appInfoManager.registerAppMetadata(appInfoManager.getInfo().getMetadata());
        appInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }
}
