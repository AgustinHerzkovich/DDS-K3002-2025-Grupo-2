package aplicacion.prometheus;

import aplicacion.config.AgregadorConfig;
import aplicacion.config.ConfigService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PrometheusStarter {

    private Process prometheusProcess;
    private final ConfigService configService;
    public PrometheusStarter(ConfigService configService){
        this.configService = configService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startPrometheus() throws IOException {
        if(!configService.isPrometheusHabilitado()) return;
        // Detectar sistema operativo
        String os = System.getProperty("os.name").toLowerCase();
        String prometheusBin;

        if (os.contains("win")) {
            // Windows: ruta al prometheus.exe
            prometheusBin = "C:\\Program Files\\Prometheus\\prometeus.exe"; // La barra de escape se escribe poniéndola dos veces. ¿Como les anduvo cuando lo probaron?
        } else {
            // Linux: ruta típica del binario en /usr/bin
            prometheusBin = "/usr/bin/prometheus";
        }

        // Tomamos el config desde resources
        File configFile = new File(getClass().getClassLoader()
                .getResource("prometheus.yml").getFile());

        if (!configFile.exists()) {
            throw new RuntimeException("No se encontró prometheus.yml en resources/");
        }

        ProcessBuilder pb = new ProcessBuilder(
                prometheusBin,
                "--config.file=" + configFile.getAbsolutePath()
        );

        pb.inheritIO(); // para ver logs de Prometheus en la consola
        prometheusProcess = pb.start();

        System.out.println("Prometheus arrancado automáticamente al levantar apiPublica!");
    }

    @EventListener(org.springframework.context.event.ContextClosedEvent.class)
    public void stopPrometheus() {
        if (!configService.isPrometheusHabilitado()) return;
        if (prometheusProcess != null) {
            prometheusProcess.destroy();
            System.out.println("Prometheus detenido.");
        }
    }
}
