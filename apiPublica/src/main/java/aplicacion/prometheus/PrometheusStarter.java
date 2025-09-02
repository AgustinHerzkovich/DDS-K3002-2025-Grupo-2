package aplicacion.prometheus;

import aplicacion.config.AgregadorConfig;
import aplicacion.config.ConfigService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class PrometheusStarter {

    private Process prometheusProcess;
    private final ConfigService configService;
    public PrometheusStarter(ConfigService configService){
        this.configService = configService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startPrometheus() throws IOException {
        if (!configService.isPrometheusHabilitado())
            return;

        // Detectar sistema operativo
        String os = System.getProperty("os.name").toLowerCase();
        String prometheusBin;
        if (os.contains("win")) {
            // Windows: ruta al prometheus.exe
            prometheusBin = "C:\\Program Files\\Prometheus\\prometheus.exe";
        } else {
            // Linux: ruta típica del binario en /usr/bin
            prometheusBin = "/usr/bin/prometheus";
        }

        // Copiar prometheus.yml de resources a un archivo temporal
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("prometheus.yml");
        if (inputStream == null) {
            throw new RuntimeException("No se encontró prometheus.yml en resources/");
        }

        File tempConfig = File.createTempFile("prometheus-", ".yml");
        tempConfig.deleteOnExit(); // se borra cuando termine la JVM

        try (FileOutputStream out = new FileOutputStream(tempConfig)) {
            inputStream.transferTo(out);
        }

        // Lanzar Prometheus con el archivo temporal como config
        ProcessBuilder pb = new ProcessBuilder(
                prometheusBin,
                "--config.file=" + tempConfig.getAbsolutePath()
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
