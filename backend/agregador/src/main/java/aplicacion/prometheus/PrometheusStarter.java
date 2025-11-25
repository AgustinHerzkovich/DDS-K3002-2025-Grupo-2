package aplicacion.prometheus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PrometheusStarter {

    private Process prometheusProcess;

    @Value("${prometheus.habilitado:false}")
    private boolean prometheusHabilitado;

    @EventListener(ApplicationReadyEvent.class)
    public void startPrometheus() throws IOException {
        if (!prometheusHabilitado)
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

        // Leer prometheus.yml desde la carpeta observabilidad en la raíz del proyecto
        // Navegamos desde user.dir (raíz del proyecto multi-módulo) hacia observabilidad/
        String projectRoot = System.getProperty("user.dir");
        // Si estamos en el módulo agregador, subimos un nivel
        File prometheusYml = new File(projectRoot, "../../observabilidad/prometheus.yml");
        if (!prometheusYml.exists()) {
            // Intentar desde la raíz directamente (por si user.dir ya es la raíz)
            prometheusYml = new File(projectRoot, "../observabilidad/prometheus.yml");
            if (!prometheusYml.exists()) {
                prometheusYml = new File(projectRoot, "observabilidad/prometheus.yml");
                if (!prometheusYml.exists()) {
                    throw new RuntimeException("No se encontró prometheus.yml. Buscado en: " + prometheusYml.getAbsolutePath());
                }
            }
        }

        System.out.println("Usando configuración de Prometheus desde: " + prometheusYml.getAbsolutePath());

        // Lanzar Prometheus con el archivo de configuración
        ProcessBuilder pb = new ProcessBuilder(
                prometheusBin,
                "--config.file=" + prometheusYml.getAbsolutePath()
        );
        pb.inheritIO(); // para ver logs de Prometheus en la consola
        prometheusProcess = pb.start();

        System.out.println("Prometheus arrancado automáticamente al levantar agregador!");
    }

    @EventListener(org.springframework.context.event.ContextClosedEvent.class)
    public void stopPrometheus() {
        if (!prometheusHabilitado) return;
        if (prometheusProcess != null) {
            prometheusProcess.destroy();
            System.out.println("Prometheus detenido.");
        }
    }
}

