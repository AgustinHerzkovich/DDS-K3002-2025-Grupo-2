package aplicacion.services.schedulers;

import aplicacion.clienteMedidorTrafico.PrometheusClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@EnableScheduling
public class CalcularHorarioBajaCargaScheduler {
    private final EjecutarAlgoritmoConsensoScheduler scheduler;
    private final PrometheusClient prometheusClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CalcularHorarioBajaCargaScheduler(EjecutarAlgoritmoConsensoScheduler scheduler,
                                             PrometheusClient prometheusClient) {
        this.scheduler = scheduler;
        this.prometheusClient = prometheusClient;
    }

    @Scheduled(cron = "0 0 0 * * SUN") // Domingo a medianoche -> se calcula y actualiza el horario de baja carga
    public void actualizarHorarioBajaCarga() throws IOException, InterruptedException {
        String promql = "sum(increase(http_server_requests_seconds_count[1h]))"; // tu query
        String json = prometheusClient.query(promql);
        JsonNode root = objectMapper.readTree(json);
        JsonNode result = root.path("data").path("result");

        int horaBaja = calcularHoraBajaCarga(result); // ahora usamos el método reescrito
        scheduler.setHoraBajaCarga(horaBaja);

        System.out.println("Hora de baja carga actualizada a las " + horaBaja + "h");
    }

    private int calcularHoraBajaCarga(JsonNode result) {
        // Mapa hora -> total de requests
        Map<Integer, Double> requestsPorHora = new HashMap<>();

        Iterator<JsonNode> elements = result.elements();
        while (elements.hasNext()) {
            JsonNode serie = elements.next();
            JsonNode valueNode = serie.path("value");
            if (valueNode.isMissingNode() || valueNode.size() < 2) continue;

            long timestampSec = valueNode.get(0).asLong(); // timestamp en segundos
            double value = valueNode.get(1).asDouble();   // valor de la métrica

            // Convertir timestamp a hora local
            ZonedDateTime zdt = Instant.ofEpochSecond(timestampSec).atZone(ZoneId.systemDefault());
            int hour = zdt.getHour();

            // Sumar al total de esa hora
            requestsPorHora.put(hour, requestsPorHora.getOrDefault(hour, 0.0) + value);
        }

        // Buscar la hora con menos requests
        int horaBaja = 0;
        double minRequests = Double.MAX_VALUE;
        for (Map.Entry<Integer, Double> entry : requestsPorHora.entrySet()) {
            if (entry.getValue() < minRequests) {
                minRequests = entry.getValue();
                horaBaja = entry.getKey();
            }
        }

        return horaBaja;
    }
}
