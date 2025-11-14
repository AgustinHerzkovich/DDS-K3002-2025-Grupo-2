package aplicacion.config.eurekaConfig;

import aplicacion.repositorios.AgregadorIDRepository;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.List;

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

        while (agregadorIDRepository.count() == 0) {
            try {
                Thread.sleep(100); // Espera 100 ms antes de volver a verificar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        agregadorID = agregadorIDRepository.findAll().get(0).getAgregadorID();

        appInfoManager.getInfo().getMetadata().put("agregadorID", agregadorID);

        appInfoManager.registerAppMetadata(appInfoManager.getInfo().getMetadata());
        appInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }
}
