package aplicacion.config.eurekaConfig;

import aplicacion.domain.agregadorID.AgregadorID;
import aplicacion.repositories.AgregadorIDRepository;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.UUID;

@Configuration
public class EurekaMetadataConfig {

    @Autowired
    private ApplicationInfoManager appInfoManager;

    private String agregadorID;

    private AgregadorIDRepository agregadorIDRepository;

    public EurekaMetadataConfig(AgregadorIDRepository agregadorIDRepository
                                , ApplicationInfoManager appInfoManager) {
        this.appInfoManager = appInfoManager;
        this.agregadorIDRepository = agregadorIDRepository;
    }
    @PostConstruct
    public void inicializarMetadata() {

        if (agregadorIDRepository.count() == 0) {
            UUID uuid = UUID.randomUUID();
            AgregadorID newAgregadorID = new AgregadorID(uuid.toString());
            agregadorIDRepository.save(newAgregadorID);
        }
        agregadorID = agregadorIDRepository.findAll().get(0).getAgregadorID();

        appInfoManager.getInfo().getMetadata().put("agregadorID", agregadorID);

        appInfoManager.registerAppMetadata(appInfoManager.getInfo().getMetadata());
        appInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }
}
