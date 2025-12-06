package aplicacion.config.metadataConfig;

import aplicacion.services.ArchivoService;
import aplicacion.services.AwsS3FileServerService;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class EurekaMetadataConfig {

    private final ApplicationInfoManager appInfoManager;
    private final AwsS3FileServerService archivoService;

    public EurekaMetadataConfig(ApplicationInfoManager appInfoManager,
                                AwsS3FileServerService archivoService) {
        this.appInfoManager = appInfoManager;
        this.archivoService = archivoService;
    }

    @PostConstruct
    public void inicializarMetadata() {
        actualizarMetadata();
        appInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }

    @Scheduled(fixedRate = 30000) // cada 30 segundos
    public void actualizarMetadataPeriodicamente() { // todo cambiar a que se actualice solo cuando haya cambios. Preguntar a herzkovich
        actualizarMetadata();
    }

    private void actualizarMetadata() {
        List<String> fuentes = archivoService.listarFuentes();

        appInfoManager.getInfo().getMetadata()
                .put("fuentes", String.join(",", fuentes));
        appInfoManager.getInfo().getMetadata().put("tipoFuente", "estatica");

        // Fuerza que Eureka reciba la actualización
        appInfoManager.refreshDataCenterInfoIfRequired();
        appInfoManager.registerAppMetadata(appInfoManager.getInfo().getMetadata());

        System.out.println("Metadatos actualizados en Eureka: " + fuentes);
    }
}
